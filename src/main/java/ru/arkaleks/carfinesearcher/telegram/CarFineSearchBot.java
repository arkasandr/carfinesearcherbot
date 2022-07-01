package ru.arkaleks.carfinesearcher.telegram;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.arkaleks.carfinesearcher.telegram.handler.CallbackQueryHandler;
import ru.arkaleks.carfinesearcher.telegram.handler.MessageHandler;

import java.io.IOException;

import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.EXCEPTION_ILLEGAL_MESSAGE;
import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.EXCEPTION_WHAT_THE_FUCK;

@Getter
@Setter
public class CarFineSearchBot extends SpringWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    MessageHandler messageHandler;

    CallbackQueryHandler callbackQueryHandler;

    public CarFineSearchBot(SetWebhook setWebhook, MessageHandler messageHandler,
                            CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    EXCEPTION_ILLEGAL_MESSAGE.getMessage());
        } catch (Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    EXCEPTION_WHAT_THE_FUCK.getMessage());
        }
    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    /**
     * Метод реализует выбор перехватчика событий.
     */

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.answerMessage(update.getMessage());
            }
        }
        return null;
    }
}
