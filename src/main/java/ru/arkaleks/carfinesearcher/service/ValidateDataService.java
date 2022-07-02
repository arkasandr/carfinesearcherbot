package ru.arkaleks.carfinesearcher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.arkaleks.carfinesearcher.telegram.constants.BotMessageEnum.*;

@Service
@Slf4j
public class ValidateDataService {

    private static final String EMPTY = "";

    public SendMessage validateUserData(String chatId, String data) {
        if (isNotBlank(data)) {
            if (data.length() == 8 || data.length() == 9 || data.length() == 10) {
                if (data.length() == 8 || data.length() == 9) {
                    if (data.toUpperCase().matches("^[A-Z]{1}\\d{3}[A-Z]{2}\\d{2}") ||
                            data.toUpperCase().matches("^[A-Z]{1}\\d{3}[A-Z]{2}\\d{3}")) {
                        log.info("Registration number is: {}", data.toUpperCase());
                        return new SendMessage(chatId, REGISTRATION_NUMBER_MESSAGE.getMessage());
                    }
                    return new SendMessage(chatId, WRONG_REGISTRATION_NUMBER_MESSAGE.getMessage());
                } else {
                    if (data.toUpperCase().matches("^\\d{2}[A-Z]{2}\\d{6}")) {
                        log.info("Certificate number is: {}", data.toUpperCase());
                        return new SendMessage(chatId, CERTIFICATE_NUMBER_MESSAGE.getMessage());
                    }
                }
            }
            return new SendMessage(chatId, EXCEPTION_WRONG_MESSAGE.getMessage());
        }
        return new SendMessage(chatId, EXCEPTION_EMPTY_MESSAGE.getMessage());


//            List<String> dataParts = asList(data.split("[\\p{Punct}\\s]+"));
//            if (dataParts.size() > 2) {
//                return new SendMessage(chatId, EXCEPTION_TOO_LONG_MESSAGE.getMessage());
//            } else if (dataParts.size() == 1) {
//                return new SendMessage(chatId, EXCEPTION_TOO_SHORT_MESSAGE.getMessage());
//            }
//            return new SendMessage(chatId, EMPTY);
//        }
//        return new SendMessage(chatId, EXCEPTION_EMPTY_MESSAGE.getMessage());
    }
}
