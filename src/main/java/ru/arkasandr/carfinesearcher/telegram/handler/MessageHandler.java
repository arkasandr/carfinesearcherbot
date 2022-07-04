package ru.arkasandr.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.service.ChatService;
import ru.arkasandr.carfinesearcher.service.ValidateDataService;
import ru.arkasandr.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;
import static ru.arkasandr.carfinesearcher.telegram.constants.ButtonNameEnum.*;


@RequiredArgsConstructor
@Slf4j
@Service
public class MessageHandler {

    private final ReplyKeyboardMaker keyboardMaker;
    private final ValidateDataService validateDataService;
    private final ChatService chatService;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        log.info("ChatId is: {}", chatId);
        var chat = chatService.findChatByChatId(chatId);
        if(isNull(chat)) {
            chat = chatService.saveChatFromMessage(message);
        }
        String inputText = message.getText();
        log.info("Message text is: {}", inputText);
        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            return getStartMessage(chatId);
        } else if (inputText.equals(SENT_BUTTON.getButtonName())) {
            return getDataMessage(chatId);
        } else if (inputText.equals(HELP_BUTTON.getButtonName())) {
            return getHelpMessage(chatId);
        } else {
            SendMessage validateMessage = validateDataService.validateUserData(chatId, inputText);
            if (validateMessage.getText().startsWith(REGISTRATION_NUMBER_MESSAGE.getMessage().substring(0, 8))) {
                chatService.saveRegistrationNumber(chat, inputText);
                log.info("RegistrationNumber is: {}", inputText);

            } else if (validateMessage.getText().startsWith(CERTIFICATE_NUMBER_MESSAGE.getMessage().substring(0, 8))) {
                chatService.saveCertificateNumber(chat, inputText);
                log.info("CertificateNumber is: {}", inputText);
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

    private SendMessage getDataMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, START_MESSAGE.getMessage());
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

}
