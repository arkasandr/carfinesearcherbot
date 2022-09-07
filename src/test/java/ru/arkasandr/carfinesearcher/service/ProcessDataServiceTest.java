package ru.arkasandr.carfinesearcher.service;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.DisplayName;import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.Mock;import org.mockito.Mockito;import org.mockito.junit.jupiter.MockitoExtension;import org.telegram.telegrambots.meta.api.methods.send.SendMessage;import ru.arkasandr.carfinesearcher.model.Car;import ru.arkasandr.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;import java.util.Optional;import static java.time.LocalDateTime.now;import static java.util.Optional.of;import static org.assertj.core.api.Assertions.assertThat;import static org.mockito.BDDMockito.given;import static org.mockito.Mockito.times;import static org.mockito.Mockito.verify;import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;@ExtendWith(MockitoExtension.class)@DisplayName("Класс ProcessDataService")class ProcessDataServiceTest {    private static final Long ID = 1L;    private static final String REGISTRATION_NUMBER = "A999AA55";    private static final String CERTIFICATE_NUMBER = "77AC123123";    private static final String CAPTCHA = "12345";    @Mock    private ReplyKeyboardMaker keyboardMaker;    @Mock    private ChatService chatService;    @Mock    private CarService carService;    @Mock    private RequestService requestService;    private ProcessDataService processDataService;    @BeforeEach    void setUp() {        processDataService = new ProcessDataService(keyboardMaker, chatService, carService, requestService);    }    @Test    @DisplayName("обрабатывает регистрационный знак и сохраняет его")    void shouldProcessAndSaveNewRegistrationNumber() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .chatId(ID)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), REGISTRATION_NUMBER_MESSAGE.getMessage());        given(carService.findCarWithoutCertificateNumber())                .willReturn(Optional.empty());        given(carService.findCarByRegistrationNumber(REGISTRATION_NUMBER))                .willReturn(Optional.empty());        assertThat(processDataService.processRegistrationNumber(chat, ID.toString(), REGISTRATION_NUMBER)).isEqualTo(sendMessage);    }    @Test    @DisplayName("обрабатывает регистрационный знак и обновляет его")    void shouldProcessAndUpdateRegistrationNumber() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .chatId(ID)                .build();        var car = Car.builder()                .id(ID)                .registrationNumber(REGISTRATION_NUMBER)                .certificateNumber(CERTIFICATE_NUMBER)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_EXISTING_REGISTRATION_NUMBER.getMessage());        given(carService.findCarWithoutCertificateNumber())                .willReturn(Optional.empty());        given(carService.findCarByRegistrationNumber(REGISTRATION_NUMBER))                .willReturn(of(car));        assertThat(processDataService.processRegistrationNumber(chat, ID.toString(), REGISTRATION_NUMBER)).isEqualTo(sendMessage);        verify(carService, times(1)).save(Mockito.any(Car.class));    }    @Test    @DisplayName("обрабатывает регистрационный знак и находит незаконченный запрос")    void shouldProcessRegistrationNumberAndFindExistingRequest() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .chatId(ID)                .build();        var car = Car.builder()                .id(ID)                .registrationNumber(REGISTRATION_NUMBER)                .certificateNumber(CERTIFICATE_NUMBER)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_EXISTING_REQUEST.getMessage());        given(carService.findCarWithoutCertificateNumber())                .willReturn(of(car));        assertThat(processDataService.processRegistrationNumber(chat, ID.toString(), REGISTRATION_NUMBER)).isEqualTo(sendMessage);    }    @Test    @DisplayName("обрабатывает номер свидетельства и сохраняет его")    void shouldProcessAndSaveCertificateNumber() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .id(ID)                .chatId(ID)                .build();        var car = Car.builder()                .id(ID)                .registrationNumber(REGISTRATION_NUMBER)                .certificateNumber(CERTIFICATE_NUMBER)                .updateDate(now())                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), CERTIFICATE_NUMBER_MESSAGE.getMessage());        sendMessage.enableMarkdown(true);        sendMessage.setReplyMarkup(keyboardMaker.getHelpMenuKeyboard());        given(carService.findCarWithRegistrationNumberAndLastUpdateDate(ID))                .willReturn(of(car));        assertThat(processDataService.processCertificateNumber(chat, ID.toString(), CERTIFICATE_NUMBER)).isEqualTo(sendMessage);    }    @Test    @DisplayName("обрабатывает номер свидетельства и подтверждает готовность данных")    void shouldProcessCertificateNumberAndSendDataReadyMessage() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .id(ID)                .chatId(ID)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), READY_DATA_MESSAGE.getMessage());        sendMessage.enableMarkdown(true);        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());        given(carService.findCarWithRegistrationNumberAndLastUpdateDate(ID))                .willReturn(Optional.empty());        given(carService.findCarIdByCertificateNumberAndLastUpdateDate(ID, CERTIFICATE_NUMBER))                .willReturn(ID);        assertThat(processDataService.processCertificateNumber(chat, ID.toString(), CERTIFICATE_NUMBER)).isEqualTo(sendMessage);    }    @Test    @DisplayName("обрабатывает номер свидетельства и не находит регистрационного знака")    void shouldProcessCertificateNumberAndNotFindRegistrationNumber() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .id(ID)                .chatId(ID)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_CERTIFICATE_BEFORE_REGISTRATION.getMessage());        given(carService.findCarWithRegistrationNumberAndLastUpdateDate(ID))                .willReturn(Optional.empty());        given(carService.findCarIdByCertificateNumberAndLastUpdateDate(ID, CERTIFICATE_NUMBER))                .willReturn(null);        assertThat(processDataService.processCertificateNumber(chat, ID.toString(), CERTIFICATE_NUMBER)).isEqualTo(sendMessage);    }    @Test    @DisplayName("успешно обрабатывает captcha")    void shouldSuccessProcessCaptcha() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .id(ID)                .chatId(ID)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), CAPTCHA_VALUE_MESSAGE.getMessage());        given(carService.findCarIdWithFullDataAndCaptchaIsWaitingStatus(ID))                .willReturn(ID);        assertThat(processDataService.processCaptcha(chat, ID.toString(), CAPTCHA)).isEqualTo(sendMessage);    }    @Test    @DisplayName("обрабатывает captcha и бросает exception")    void shouldSuccessProcessCaptchaAndThrowException() {        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()                .id(ID)                .chatId(ID)                .build();        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_CAPTCHA_WAITING_REQUEST.getMessage());        given(carService.findCarIdWithFullDataAndCaptchaIsWaitingStatus(ID))                .willReturn(null);        assertThat(processDataService.processCaptcha(chat, ID.toString(), CAPTCHA)).isEqualTo(sendMessage);    }}