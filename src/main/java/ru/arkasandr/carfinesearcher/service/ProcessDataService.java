package ru.arkasandr.carfinesearcher.service;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.telegram.telegrambots.meta.api.methods.send.SendMessage;import ru.arkasandr.carfinesearcher.model.Chat;import ru.arkasandr.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;import static java.time.LocalDateTime.now;import static java.util.Objects.isNull;import static ru.arkasandr.carfinesearcher.service.util.GenerateSendMessageUtil.generateSendMessageWithKeyboard;import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;@Service@RequiredArgsConstructor@Slf4jpublic class ProcessDataService {    private final ReplyKeyboardMaker keyboardMaker;    private final ChatService chatService;    private final CarService carService;    private final RequestService requestService;    /**     * Метод обрабатывает регистрационный номер и формирует ответ пользователю     */    @Transactional    public SendMessage processRegistrationNumber(Chat chat, String chatId, String registrationNumber) {        SendMessage result;        var existCarWithoutCertificateNumber = carService.findCarWithoutCertificateNumber();        if (existCarWithoutCertificateNumber.isEmpty()) {            var existCar = carService.findCarByRegistrationNumber(registrationNumber);            if (existCar.isEmpty()) {                chatService.saveRegistrationNumber(chat, registrationNumber);                log.info("RegistrationNumber is: {}", registrationNumber);                result = new SendMessage(chatId, REGISTRATION_NUMBER_MESSAGE.getMessage());            } else {                existCar.ifPresent(c -> {                    c.setUpdateDate(now());                    carService.save(c);                });                result = new SendMessage(chatId, EXCEPTION_EXISTING_REGISTRATION_NUMBER.getMessage());            }        } else {            result = new SendMessage(chatId, EXCEPTION_EXISTING_REQUEST.getMessage());        }        return result;    }    /**     * Метод обрабатывает номер свидетельства о регистрации и формирует ответ пользователю     */    @Transactional    public SendMessage processCertificateNumber(Chat chat, String chatId, String certificateNumber) {        SendMessage result;        var existCar = carService.findCarWithRegistrationNumberAndLastUpdateDate(chat.getId());        if (existCar.isPresent()) {            existCar.ifPresent(car -> {                        chatService.saveCertificateNumber(chat, car, certificateNumber);                        log.info("CertificateNumber is: {}", certificateNumber);                    }            );            result = generateSendMessageWithKeyboard(chatId, CERTIFICATE_NUMBER_MESSAGE.getMessage(),                    keyboardMaker.getMainMenuKeyboard());        } else {            result = isNull(carService.findCarIdByCertificateNumberAndLastUpdateDate(chat.getId(), certificateNumber))                    ? new SendMessage(chatId, EXCEPTION_CERTIFICATE_BEFORE_REGISTRATION.getMessage())                    : generateSendMessageWithKeyboard(chatId, READY_DATA_MESSAGE.getMessage(),                    keyboardMaker.getMainMenuKeyboard());        }        return result;    }    /**     * Метод обрабатывает captcha, присланную в виде рисунка, и формирует ответ пользователю     */    @Transactional    public SendMessage processCaptcha(Chat chat, String chatId, String captcha) {        SendMessage result;        var carId = carService.findCarIdWithFullDataAndCaptchaIsWaitingStatus(chat.getId());        if (!isNull(carId)) {            requestService.increaseCaptchaAttempt(carId);            requestService.sendRequestWithCaptchaValueToParser(carId, captcha);            result = new SendMessage(chatId, CAPTCHA_VALUE_MESSAGE.getMessage());        } else {            result = new SendMessage(chatId, EXCEPTION_CAPTCHA_WAITING_REQUEST.getMessage());        }        return result;    }}