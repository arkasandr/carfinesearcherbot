package ru.arkasandr.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.model.Chat;
import ru.arkasandr.carfinesearcher.service.CarService;
import ru.arkasandr.carfinesearcher.service.ChatService;
import ru.arkasandr.carfinesearcher.service.RequestProcessService;
import ru.arkasandr.carfinesearcher.service.ValidateDataService;
import ru.arkasandr.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;
import static ru.arkasandr.carfinesearcher.telegram.constants.ButtonNameEnum.HELP_BUTTON;
import static ru.arkasandr.carfinesearcher.telegram.constants.ButtonNameEnum.SENT_BUTTON;


@RequiredArgsConstructor
@Slf4j
@Service
public class MessageHandler {

    private static final String USER_START = "/start";

    private final ReplyKeyboardMaker keyboardMaker;
    private final ValidateDataService validateDataService;
    private final ChatService chatService;
    private final CarService carService;
    private final RequestProcessService requestProcessService;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        log.info("ChatId is: {}", chatId);
        var chat = chatService.findChatByChatId(chatId);
        if (isNull(chat)) {
            chat = chatService.saveChatFromMessage(message);
        }
        String inputText = message.getText();
        log.info("Message text is: {}", inputText);
        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals(USER_START)) {
            return getStartMessage(chatId);
        } else if (inputText.equals(SENT_BUTTON.getButtonName())) {
            return getDataMessage(chat, chatId);
        } else if (inputText.equals(HELP_BUTTON.getButtonName())) {
            return getHelpMessage(chatId);
        } else {
            SendMessage validateMessage = validateDataService.validateUserData(chatId, inputText);
            if (validateMessage.getText().startsWith(REGISTRATION_NUMBER_MESSAGE.getMessage().substring(0, 8))) {
                validateMessage = processRegistrationNumber(chat, chatId, inputText);
            } else if (validateMessage.getText().startsWith(CERTIFICATE_NUMBER_MESSAGE.getMessage().substring(0, 8))) {
                validateMessage = processCertificateNumber(chat, chatId, inputText);
            }
            return isBlank(validateMessage.getText())
                    ? new SendMessage(chatId, SUCCESS_DATA_SENDING.getMessage())
                    : validateMessage;
        }
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, START_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getDataMessage(Chat chat, String chatId) {
        var carId = carService.findCarIdWithFullDataAndNotInSendingStatus(chat.getId());
        var sendMessage = isNull(carId)
                ? new SendMessage(chatId, START_MESSAGE.getMessage())
                : sendRequestToPlatform(chatId, carId);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getHelpMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage sendRequestToPlatform(String chatId, Long id) {
        requestProcessService.sendRequest(id);
        return new SendMessage(chatId, SUCCESS_DATA_SENDING.getMessage());
    }

    private SendMessage processRegistrationNumber(Chat chat, String chatId, String inputText) {
        SendMessage result = new SendMessage();
        var existCarWithoutCertificateNumber = carService.findCarWithoutCertificateNumber();
        if (existCarWithoutCertificateNumber.isEmpty()) {
            var existCar = carService.findCarByRegistrationNumber(inputText);
            if (existCar.isEmpty()) {
                chatService.saveRegistrationNumber(chat, inputText);
                log.info("RegistrationNumber is: {}", inputText);
            } else {
                result = new SendMessage(chatId, EXCEPTION_EXISTING_REGISTRATION_NUMBER.getMessage());
            }
        } else {
            result = new SendMessage(chatId, EXCEPTION_EXISTING_REQUEST.getMessage());
        }
        return result;
    }

    private SendMessage processCertificateNumber(Chat chat, String chatId, String inputText) {
        SendMessage result = new SendMessage();
        var existCar = carService.findCarByChatIdAndCertificateNumberIsNull(chat.getId());
        if (existCar.isPresent()) {
            existCar.ifPresent(car -> {
                        chatService.saveCertificateNumber(chat, car, inputText);
                        log.info("CertificateNumber is: {}", inputText);
                    }
            );
        } else {
            result = isNull(carService.findCarIdWithFullDataAndNotInSendingStatus(chat.getId()))
                    ? new SendMessage(chatId, EXCEPTION_CERTIFICATE_BEFORE_REGISTRATION.getMessage())
                    : new SendMessage(chatId, READY_DATA_MESSAGE.getMessage());
        }
        return result;
    }
}
