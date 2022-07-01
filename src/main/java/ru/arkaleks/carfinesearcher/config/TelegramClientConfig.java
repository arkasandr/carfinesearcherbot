package ru.arkaleks.carfinesearcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.arkaleks.carfinesearcher.telegram.CarFineSearchBot;
import ru.arkaleks.carfinesearcher.telegram.handler.CallbackQueryHandler;
import ru.arkaleks.carfinesearcher.telegram.handler.MessageHandler;

@Configuration
public class TelegramClientConfig {

    @Value("${telegram.webhook}")
    String webhookPath;

    @Value("${telegram.name}")
    String botName;

    @Value("${telegram.token}")
    String botToken;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(webhookPath).build();
    }

    @Bean
    public CarFineSearchBot springWebookBot(SetWebhook setWebhook,
                                            MessageHandler messageHandler,
                                            CallbackQueryHandler callbackQueryHandler) {
        CarFineSearchBot bot = new CarFineSearchBot(setWebhook, messageHandler, callbackQueryHandler);
        bot.setBotPath(webhookPath);
        bot.setBotUsername(botName);
        bot.setBotToken(botToken);
        return bot;
    }
}
