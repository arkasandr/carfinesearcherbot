package ru.arkasandr.carfinesearcher.telegram.handler;

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
        return new SendMessage(chatId, "Type car registration number like A000AA777");
    }
}
