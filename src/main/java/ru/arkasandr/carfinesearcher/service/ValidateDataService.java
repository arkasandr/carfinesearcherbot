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
    private static final Integer CAPTCHA_VALUE_POSITION = 16;

    public SendMessage validateUserData(String chatId, String data) {
        if (isNotBlank(data)) {
            if (data.length() == 8 || data.length() == 9 || data.length() == 10) {
                if (data.length() == 8 || data.length() == 9) {
                    if (data.matches(".*[a-zA-Z]+.*")) {
                        return new SendMessage(chatId, EXCEPTION_WRONG_RUSSIAN_LANGUAGE_MESSAGE.getMessage());
                    } else if (data.toUpperCase().matches("^[ABCEHKMOPTYXАВЕКМНОРСТУХ]{1}\\d{3}[ABCEHKMOPTYXАВЕКМНОРСТУХ]{2}\\d{2}")
                            || data.toUpperCase().matches("^[ABCEHKMOPTYXАВЕКМНОРСТУХ]{1}\\d{3}[ABCEHKMOPTYXАВЕКМНОРСТУХ]{2}\\d{3}")) {
                        log.info("Registration number is: {}", data.toUpperCase());
                        return new SendMessage(chatId, getRegistrationNumberMessage(data.toUpperCase()));
                    }
                    return new SendMessage(chatId, WRONG_REGISTRATION_NUMBER_MESSAGE.getMessage());
                } else {
                    if (data.matches(".*[а-яА-Я]+.*") && data.toUpperCase().matches("^\\d{2}[а-яА-Я]{2}\\d{6}")) {
                        return new SendMessage(chatId, EXCEPTION_WRONG_ENGLISH_LANGUAGE_MESSAGE.getMessage());
                    }
                    if (data.toUpperCase().matches("^\\d{2}[ABCEHKMOPTYXАВЕКМНОРСТУХ]{2}\\d{6}")
                            || data.toUpperCase().matches("^\\d{10}")) {
                        log.info("Certificate number is: {}", data.toUpperCase());
                        return new SendMessage(chatId, getCertificateNumberMessage(data.toUpperCase()));
                    }
                    return new SendMessage(chatId, WRONG_CERTIFICATE_NUMBER_MESSAGE.getMessage());
                }
            } else if (data.length() == 5) {
                if (data.toUpperCase().matches("^\\d{5}")) {
                    log.info("Captcha value is: {}", data.toUpperCase());
                    return new SendMessage(chatId, getCaptchaValueMessage(data.toUpperCase()));
                }
                return new SendMessage(chatId, WRONG_CAPTCHA_VALUE_MESSAGE.getMessage());
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

    private String getCaptchaValueMessage(String captcha) {
        return CAPTCHA_VALUE_MESSAGE.getMessage().substring(0, CAPTCHA_VALUE_POSITION)
                + captcha + " "
                + CAPTCHA_VALUE_MESSAGE.getMessage().substring(CAPTCHA_VALUE_POSITION);
    }

}
