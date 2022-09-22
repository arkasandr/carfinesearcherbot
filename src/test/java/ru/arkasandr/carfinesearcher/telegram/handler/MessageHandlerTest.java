package ru.arkasandr.carfinesearcher.telegram.handler;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.DisplayName;import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.Mock;import org.mockito.junit.jupiter.MockitoExtension;import org.telegram.telegrambots.meta.api.methods.send.SendMessage;import org.telegram.telegrambots.meta.api.objects.Chat;import org.telegram.telegrambots.meta.api.objects.Message;import ru.arkasandr.carfinesearcher.service.*;import ru.arkasandr.carfinesearcher.telegram.keyboards.ReplyKeyboardMaker;import static org.assertj.core.api.Assertions.assertThat;import static org.mockito.BDDMockito.given;import static ru.arkasandr.carfinesearcher.telegram.constants.BotMessageEnum.*;import static ru.arkasandr.carfinesearcher.telegram.constants.ButtonNameEnum.HELP_BUTTON;@ExtendWith(MockitoExtension.class)@DisplayName("Класс MessageHandler")class MessageHandlerTest {    private static final Long ID = 1L;    private static final String REGISTRATION_NUMBER = "A999AA55";    private static final String CERTIFICATE_NUMBER = "77AC123123";    @Mock    private ReplyKeyboardMaker keyboardMaker;    @Mock    private ValidateDataService validateDataService;    @Mock    private ChatService chatService;    @Mock    private CarService carService;    @Mock    private RequestService requestService;    @Mock    private ProcessDataService processDataService;    private MessageHandler messageHandler;    @BeforeEach    void setUp() {        messageHandler = new MessageHandler(keyboardMaker, validateDataService, chatService, carService,                requestService, processDataService);    }    @Test    @DisplayName("пользователь ввел /start")    void whenInputTextIsStartThenSendStartMessage() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText("/start");        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        SendMessage sendMessage = new SendMessage(ID.toString(), START_MESSAGE.getMessage());        sendMessage.enableMarkdown(true);        sendMessage.enableHtml(true);        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь нажал справку")    void whenInputTextIsHelpThenSendHelpMessage() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(HELP_BUTTON.getButtonName());        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        SendMessage sendMessage = new SendMessage(ID.toString(), HELP_MESSAGE.getMessage());        sendMessage.enableMarkdown(true);        sendMessage.enableHtml(true);        sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyboard());        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь ввел корректный регистрационный номер и в системе нет записей без свидетельства о регистрации")    void whenInputTextIsCorrectRegNumberAndNoEntriesWithoutCertNumberThenSendRegNumberMessage() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(REGISTRATION_NUMBER);        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        SendMessage sendMessage = new SendMessage(ID.toString(), REGISTRATION_NUMBER_MESSAGE.getMessage());        given(validateDataService.validateUserData(ID.toString(), REGISTRATION_NUMBER))                .willReturn(new SendMessage(ID.toString(), REGISTRATION_NUMBER_MESSAGE.getMessage()));        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        given(processDataService.processRegistrationNumber(userChat, ID.toString(), REGISTRATION_NUMBER))                .willReturn(sendMessage);        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь ввел корректный регистрационный номер и в системе есть записи без свидетельства о регистрации")    void whenInputTextIsCorrectRegNumberAndHaveEntriesWithoutCertNumberThenSendExistRequestException() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(REGISTRATION_NUMBER);        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_EXISTING_REQUEST.getMessage());        given(validateDataService.validateUserData(ID.toString(), REGISTRATION_NUMBER))                .willReturn(new SendMessage(ID.toString(), REGISTRATION_NUMBER_MESSAGE.getMessage()));        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        given(processDataService.processRegistrationNumber(userChat, ID.toString(), REGISTRATION_NUMBER))                .willReturn(sendMessage);        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь ввел корректный регистрационный номер и в системе есть запись c таким же номером")    void whenInputTextIsCorrectRegNumberAndHaveEntrWithSameRegNumberThenSendExistRegNumberException() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(REGISTRATION_NUMBER);        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_EXISTING_REGISTRATION_NUMBER.getMessage());        given(validateDataService.validateUserData(ID.toString(), REGISTRATION_NUMBER))                .willReturn(new SendMessage(ID.toString(), REGISTRATION_NUMBER_MESSAGE.getMessage()));        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        given(processDataService.processRegistrationNumber(userChat, ID.toString(), REGISTRATION_NUMBER))                .willReturn(sendMessage);        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь ввел корректное свидетельство о регистрации ранее, чем регистрационный номер")    void whenInputTextIsCorrectCertNumberBeforeRegNumberThenCertBeforeRegException() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(CERTIFICATE_NUMBER);        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        SendMessage sendMessage = new SendMessage(ID.toString(), EXCEPTION_CERTIFICATE_BEFORE_REGISTRATION.getMessage());        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        given(validateDataService.validateUserData(ID.toString(), CERTIFICATE_NUMBER))                .willReturn(new SendMessage(ID.toString(), CERTIFICATE_NUMBER_MESSAGE.getMessage()));        given(processDataService.processCertificateNumber(userChat, ID.toString(), CERTIFICATE_NUMBER))                .willReturn(sendMessage);        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь ввел корректное свидетельство о регистрации и данные отправились")    void whenInputTextIsCorrectCertNumberAndRequestSend() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(CERTIFICATE_NUMBER);        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        SendMessage sendMessage = new SendMessage(ID.toString(), CERTIFICATE_NUMBER_MESSAGE.getMessage());        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        given(validateDataService.validateUserData(ID.toString(), CERTIFICATE_NUMBER))                .willReturn(new SendMessage(ID.toString(), CERTIFICATE_NUMBER_MESSAGE.getMessage()));        given(processDataService.processCertificateNumber(userChat, ID.toString(), CERTIFICATE_NUMBER))                .willReturn(sendMessage);        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }    @Test    @DisplayName("пользователь ввел корректное свидетельство о регистрации и данные готовы к отправлению")    void whenInputTextIsCorrectCertNumberAndDataIsReady() {        Message message = new Message();        Chat chat = new Chat();        chat.setId(ID);        message.setText(CERTIFICATE_NUMBER);        message.setChat(chat);        ru.arkasandr.carfinesearcher.model.Chat userChat = new ru.arkasandr.carfinesearcher.model.Chat();        userChat.setId(ID);        SendMessage sendMessage = new SendMessage(ID.toString(), READY_DATA_MESSAGE.getMessage());        given(chatService.findChatByChatId(ID.toString()))                .willReturn(userChat);        given(validateDataService.validateUserData(ID.toString(), CERTIFICATE_NUMBER))                .willReturn(new SendMessage(ID.toString(), CERTIFICATE_NUMBER_MESSAGE.getMessage()));        given(processDataService.processCertificateNumber(userChat, ID.toString(), CERTIFICATE_NUMBER))                .willReturn(sendMessage);        assertThat(messageHandler.answerMessage(message)).isEqualTo(sendMessage);    }}