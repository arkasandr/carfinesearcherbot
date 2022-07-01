package ru.arkaleks.carfinesearcher.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {


    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {

        final String chatId = buttonQuery.getMessage().getChatId().toString();
        String data = buttonQuery.getData();

//        if(data.equals("prefix" + CallbackDataPartsEnum.NEW_MANUAL_STOCK.getCallbackDataParts())){
//            return setManualFile(chatId);
//        } else if(data.equals("prefix" + CallbackDataPartsEnum.NEW_FILE_STOCK.getCallbackDataParts())){
//            return setUploadFile(chatId);
//        } else if(data.equals("prefix" + CallbackDataPartsEnum.REDACT_STOCK.getCallbackDataParts())){
//            return redactFile(chatId);
//        } else if(data.equals("prefix" + CallbackDataPartsEnum.GET_ALL_STOCKS.getCallbackDataParts())){
//            return getAllStocks(chatId);
//        } else if(data.equals("prefix" + CallbackDataPartsEnum.FIND_STOCK_BY_ID.getCallbackDataParts())){
//            return findStockById(chatId);
//        } else if(data.equals("prefix" + CallbackDataPartsEnum.DELETE_STOCK.getCallbackDataParts())){
//            return deleteStock(chatId);
//        } else if(data.equals("prefix" + CallbackDataPartsEnum.FIND_STOCK_BY_TICKER.getCallbackDataParts())){
//            return findStockByTicker(chatId);
//        } else {
        return new SendMessage(chatId, "Type car registration number like A000AA777");
//        }
    }

//    private SendMessage setManualFile(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду /newStock и данные акции через запятую");
//    }
//    private SendMessage setUploadFile(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду /newFile и путь к файлу после запятой");
//    }
//    private SendMessage redactFile(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду  и Id редактируемой акции после запятой");
//    }
//    private SendMessage getAllStocks(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду");
//    }
//    private SendMessage findStockById(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду /findStock и Id акции после запятой");
//    }
//    private SendMessage deleteStock(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду /delete и Id акции после запятой");
//    }
//    private SendMessage findStockByTicker(String chatId){
//        return new SendMessage(chatId, "Введите в строке команду и Id акции после запятой");
//    }
}
