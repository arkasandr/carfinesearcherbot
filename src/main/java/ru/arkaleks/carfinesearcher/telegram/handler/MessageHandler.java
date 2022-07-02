package ru.arkaleks.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkaleks.carfinesearcher.service.MessageService;
import ru.arkaleks.carfinesearcher.service.ValidateDataService;
import ru.arkaleks.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.*;
import static ru.arkaleks.carfinesearcher.telegram.constants.ButtonNameEnum.*;


@RequiredArgsConstructor
@Slf4j
@Service
public class MessageHandler {

    private final ReplyKeyboardMaker keyboardMaker;
    private final ValidateDataService validateDataService;
    private final MessageService messageService;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        log.info("ChatId is: {}", chatId);
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
            if (REGISTRATION_NUMBER_MESSAGE.getMessage().equals(validateMessage.getText())) {

            } else if (CERTIFICATE_NUMBER_MESSAGE.getMessage().equals(validateMessage.getText())) {

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
