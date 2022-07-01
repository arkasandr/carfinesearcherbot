package ru.arkaleks.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkaleks.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;

import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.HELP_MESSAGE;


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
//        } else if (inputText.equals(ButtonNameEnum.SET_DATA_BUTTON.getButtonName())) {
//            return getDataMessage(chatId);
//        } else if (inputText.equals(ButtonNameEnum.REDACT_DATA_BUTTON.getButtonName())) {
//            return getRedactMessage(chatId);
//        } else if (inputText.startsWith("/newStock")){
//            var newStock = convertStringToStock(inputText);
//            baseService.saveStock(newStock);
//        } else if (inputText.startsWith("/newFile")){
//            String[] fileData = inputText.split(",");
//            baseService.uploadFile(fileData[1]);
//        } else if (inputText.startsWith("/delete")){
//            String[] inputData = inputText.split(",");
//            baseService.deleteStock(Long.valueOf(inputData[1]));
//        } else if (inputText.startsWith("/findStock")){
//            String[] inputData = inputText.split(",");
//            return getStockByIdMessage(chatId, inputData[1]);
//        } else {
//            System.out.println("Fucking text");
        }
        return null;
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

//    private SendMessage getDataMessage(String chatId) {
//        SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.CHOOSE_METHOD_MESSAGE.getMessage());
//        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons("prefix", true
//        ));
//        return sendMessage;
//    }
//
//    private SendMessage getRedactMessage(String chatId) {
//        SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.REDACT_MENU_MESSAGE.getMessage());
//        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineRedactButtons("prefix"));
//        return sendMessage;
//    }
//    private SendMessage getStockByIdMessage(String chatId, String stockId) {
//        Stock stock = baseService.getStockById(Long.valueOf(stockId));
//        SendMessage sendMessage = new SendMessage(chatId, convertStockToString(stock));
//        return sendMessage;
//    }
}
