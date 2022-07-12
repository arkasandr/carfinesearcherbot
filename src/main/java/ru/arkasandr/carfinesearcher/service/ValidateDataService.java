package ru.arkasandr.carfinesearcher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;

@Service
@Slf4j
public class ValidateDataService {

    private static final Integer REG_NUMBER_POSITION = 21;
    private static final Integer CERT_NUMBER_POSITION = 29;

    public SendMessage validateUserData(String chatId, String data) {
        if (isNotBlank(data)) {
            if (data.length() == 8 || data.length() == 9 || data.length() == 10) {
                if (data.length() == 8 || data.length() == 9) {
                    if (data.toUpperCase().matches("^[A-ZА-Я]{1}\\d{3}[A-ZА-Я]{2}\\d{2}")
                            || data.toUpperCase().matches("^[A-ZА-Я]{1}\\d{3}[A-ZА-Я]{2}\\d{3}")) {
                        log.info("Registration number is: {}", data.toUpperCase());
                        return new SendMessage(chatId, getRegistrationNumberMessage(data.toUpperCase()));
                    }
                    return new SendMessage(chatId, WRONG_REGISTRATION_NUMBER_MESSAGE.getMessage());
                } else {
                    if (data.toUpperCase().matches("^\\d{2}[A-ZА-Я]{2}\\d{6}")) {
                        log.info("Certificate number is: {}", data.toUpperCase());
                        return new SendMessage(chatId, getCertificateNumberMessage(data.toUpperCase()));
                    }
                    return new SendMessage(chatId, WRONG_CERTIFICATE_NUMBER_MESSAGE.getMessage());
                }
            }
            return new SendMessage(chatId, EXCEPTION_WRONG_MESSAGE.getMessage());
        }
        return new SendMessage(chatId, EXCEPTION_EMPTY_MESSAGE.getMessage());
    }

    private String getRegistrationNumberMessage(String regNumber) {
       return REGISTRATION_NUMBER_MESSAGE.getMessage().substring(0, REG_NUMBER_POSITION)
                + regNumber + " "
                + REGISTRATION_NUMBER_MESSAGE.getMessage().substring(REG_NUMBER_POSITION);
    }

    private String getCertificateNumberMessage(String certNumber) {
        return CERTIFICATE_NUMBER_MESSAGE.getMessage().substring(0, CERT_NUMBER_POSITION)
                + certNumber + " "
                + CERTIFICATE_NUMBER_MESSAGE.getMessage().substring(CERT_NUMBER_POSITION);
    }

}
