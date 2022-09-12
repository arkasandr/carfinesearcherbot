package ru.arkasandr.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.model.Chat;
import ru.arkasandr.carfinesearcher.service.*;
import ru.arkasandr.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.arkasandr.carfinesearcher.service.util.GenerateSendMessageUtil.generateSendMessageWithKeyboard;
import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;
import static ru.arkasandr.carfinesearcher.telegram.constants.ButtonNameEnum.HELP_BUTTON;
import static ru.arkasandr.carfinesearcher.telegram.constants.ButtonNameEnum.SENT_BUTTON;


@RequiredArgsConstructor
@Slf4j
@Service
public class MessageHandler {

    private static final String USER_START = "/start";
    private static final Integer START_MESSAGE_SYMBOL = 0;
    private static final Integer END_MESSAGE_SYMBOL = 8;


    private final ReplyKeyboardMaker keyboardMaker;
    private final ValidateDataService validateDataService;
    private final ChatService chatService;
    private final CarService carService;
    private final RequestService requestService;
    private final ProcessDataService processDataService;



    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        log.info("ChatId is: {}", chatId);
        var chat = chatService.findChatByChatId(chatId);
        if (isNull(chat.getId())) {
            chat = chatService.saveChatFromMessage(message);
        }
        String inputText = message.getText();
        log.debug("Message text is: {}", inputText);
        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals(USER_START)) {
            return generateSendMessageWithKeyboard(chatId, START_MESSAGE.getMessage(),
                    keyboardMaker.getHelpMenuKeyboard());
        } else if (inputText.equals(SENT_BUTTON.getButtonName())) {
            return getSendRequestMessage(chat, chatId);
        } else if (inputText.equals(HELP_BUTTON.getButtonName())) {
            return generateSendMessageWithKeyboard(chatId, HELP_MESSAGE.getMessage(),
                    keyboardMaker.getHelpMenuKeyboard());
        } else {
            SendMessage validateMessage = validateDataService.validateUserData(chatId, inputText);
            if (validateMessage.getText().startsWith(REGISTRATION_NUMBER_MESSAGE.getMessage()
                    .substring(START_MESSAGE_SYMBOL, END_MESSAGE_SYMBOL))) {
                validateMessage = getRegistrationNumberMessage(chat, chatId, inputText);
            } else if (validateMessage.getText().startsWith(CERTIFICATE_NUMBER_MESSAGE.getMessage()
                    .substring(START_MESSAGE_SYMBOL, END_MESSAGE_SYMBOL))) {
                validateMessage = processDataService.processCertificateNumber(chat, chatId, inputText);
                if (CERTIFICATE_NUMBER_MESSAGE.getMessage().equals(validateMessage.getText())
                        || READY_DATA_MESSAGE.getMessage().equals(validateMessage.getText())) {
                    requestService.saveReadyForSendRequest(chatId);
                }
            } else if (validateMessage.getText().startsWith(CAPTCHA_VALUE_MESSAGE.getMessage()
                    .substring(START_MESSAGE_SYMBOL, END_MESSAGE_SYMBOL))) {
                validateMessage = processDataService.processCaptcha(chat, chatId, inputText);
            }
            return isBlank(validateMessage.getText())
                    ? new SendMessage(chatId, SUCCESS_DATA_SENDING.getMessage())
                    : validateMessage;
        }
    }

    private SendMessage getSendRequestMessage(Chat chat, String chatId) {
        var carId = carService.findCarIdWithFullDataAndLastUpdateDate(chat.getId());
        var sendMessage = isNull(carId)
                ? generateSendMessageWithKeyboard(chatId, START_MESSAGE.getMessage(),
                keyboardMaker.getHelpMenuKeyboard())
                : sendRequestToGibdd(chatId, carId);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getHelpMenuKeyboard());
        return sendMessage;
    }

    private SendMessage sendRequestToGibdd(String chatId, Long carId) {
        requestService.sendRequestWithCarDataToParser(carId);
        return generateSendMessageWithKeyboard(chatId, SUCCESS_DATA_SENDING.getMessage(),
                keyboardMaker.getHelpMenuKeyboard());
    }

    private SendMessage getRegistrationNumberMessage(Chat chat, String chatId, String registrationNumber) {
        return processDataService.processRegistrationNumber(chat, chatId, registrationNumber);
    }

}