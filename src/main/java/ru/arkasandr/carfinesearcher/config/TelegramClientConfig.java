package ru.arkasandr.carfinesearcher.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import ru.arkasandr.carfinesearcher.config.props.TelegramProperties;
import ru.arkasandr.carfinesearcher.telegram.CarFineSearchBot;
import ru.arkasandr.carfinesearcher.telegram.handler.CallbackQueryHandler;
import ru.arkasandr.carfinesearcher.telegram.handler.MessageHandler;

@Configuration
@Data
public class TelegramClientConfig {

    private final TelegramProperties telegramProperties;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramProperties.getWebhook()).build();
    }

    @Bean
    public CarFineSearchBot springWebhookBot(SetWebhook setWebhook,
                                            MessageHandler messageHandler,
                                            CallbackQueryHandler callbackQueryHandler) {
        CarFineSearchBot bot = new CarFineSearchBot(setWebhook, messageHandler, callbackQueryHandler);
        bot.setBotPath(telegramProperties.getWebhook());
        bot.setBotUsername(telegramProperties.getName());
        bot.setBotToken(telegramProperties.getToken());
        return bot;
    }
}
