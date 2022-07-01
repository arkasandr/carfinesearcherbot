package ru.arkaleks.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkaleks.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;

import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.HELP_MESSAGE;
import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.SUCCESS_DATA_SENDING;
import static ru.arkaleks.carfinesearcher.telegram.constants.ButtonNameEnum.HELP_BUTTON;
import static ru.arkaleks.carfinesearcher.telegram.constants.ButtonNameEnum.SENT_BUTTON;


@RequiredArgsConstructor
@Slf4j
@Service
public class MessageHandler {

    private final ReplyKeyboardMaker keyboardMaker;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        log.info("ChatId is {}", chatId);
        String inputText = message.getText();
        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            return getStartMessage(chatId);
        } else if (inputText.equals(SENT_BUTTON.getButtonName())) {
            return getDataMessage(chatId);
        } else if (inputText.equals(HELP_BUTTON.getButtonName())) {
            return getStartMessage(chatId);
        } else {
            System.out.println("Fucking text");
        }
        return null;
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getDataMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, SUCCESS_DATA_SENDING.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

}
