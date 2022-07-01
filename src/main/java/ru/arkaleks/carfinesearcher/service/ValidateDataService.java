package ru.arkaleks.carfinesearcher.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.*;

@Service
public class ValidateDataService {

    private static final String EMPTY = "";

    public SendMessage validateUserData(String chatId, String data) {
        if(isNotBlank(data)) {
            List<String> dataParts = asList(data.split("[\\p{Punct}\\s]+"));
            if (dataParts.size() > 2) {
                return new SendMessage(chatId, EXCEPTION_TOO_LONG_MESSAGE.getMessage());
            } else if (dataParts.size() == 1) {
                return new SendMessage(chatId, EXCEPTION_TOO_SHORT_MESSAGE.getMessage());
            }
            return new SendMessage(chatId, EMPTY);
        }
        return new SendMessage(chatId, EXCEPTION_EMPTY_MESSAGE.getMessage());
    }
}
