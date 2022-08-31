package ru.arkasandr.carfinesearcher.service.message;import com.fasterxml.jackson.core.JsonProcessingException;import com.fasterxml.jackson.databind.ObjectMapper;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.DisplayName;import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.Mock;import org.mockito.Mockito;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.messaging.Message;import org.springframework.messaging.support.MessageBuilder;import org.springframework.web.client.RestTemplate;import ru.arkasandr.carfinesearcher.model.Car;import ru.arkasandr.carfinesearcher.model.GibddRequest;import ru.arkasandr.carfinesearcher.model.enums.RequestStatus;import ru.arkasandr.carfinesearcher.repository.ChatRepository;import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;import ru.arkasandr.carfinesearcher.service.GibddScrapService;import ru.arkasandr.carfinesearcher.service.message.dto.Answer;import ru.arkasandr.carfinesearcher.service.message.dto.CaptchaMessageRequestDto;import ru.arkasandr.carfinesearcher.service.message.dto.CaptchaMessageResponseDto;import ru.arkasandr.carfinesearcher.service.message.dto.GibddRequestMessageDto;import java.util.Set;import java.util.UUID;import static java.time.LocalDateTime.now;import static org.assertj.core.api.Java6Assertions.assertThat;import static org.mockito.BDDMockito.given;import static org.mockito.Mockito.times;import static org.mockito.Mockito.verify;@ExtendWith(MockitoExtension.class)@DisplayName("Класс MessageServiceTest")class MessageServiceTest {    private static final Long ID = 1L;    private static final String UUID_NUMBER = "73f17c7d-b897-443e-8181-ffadaa9bb36f";    private static final String REGISTRATION_NUMBER = "A999AA55";    private static final String CERTIFICATE_NUMBER = "77AC123123";    private static final String CAPTCHA = "12345";    @Mock    private MessageGateway gateway;    @Mock    private GibddRequestRepository requestRepository;    @Mock    private GibddScrapService scrapService;    @Mock    private RestTemplate restTemplate;    @Mock    private ChatRepository chatRepository;    private MessageService messageService;    @BeforeEach    void setUp() {        messageService = new MessageService(gateway, scrapService, restTemplate, chatRepository, requestRepository);    }    @Test    @DisplayName("отправление запроса c данными о ТС в очередь (от бота парсеру)")    void shouldSendMessageToQueueWithCarData() {        Car car = Car.builder()                .id(ID)                .registrationNumber(REGISTRATION_NUMBER)                .certificateNumber(CERTIFICATE_NUMBER)                .build();        GibddRequest request = GibddRequest.builder()                .id(UUID.fromString(UUID_NUMBER))                .createDate(now())                .requestDate(now())                .car(car)                .build();        car.setRequest(Set.of(request));        given(requestRepository.save(request))                .willReturn(request);        messageService.sendMessageToQueueWithCarData(car);        assertThat(request.getStatus())                .isEqualTo(RequestStatus.SENDING);        verify(requestRepository, times(1)).save(Mockito.any(GibddRequest.class));    }    @Test    @DisplayName("получение запроса c данными о ТС из очереди (парсером)")    void shouldReceiveMessageFromQueueWithCarData() throws JsonProcessingException {        var requestMessage = GibddRequestMessageDto.builder()                .registrationNumber(REGISTRATION_NUMBER)                .certificateNumber(CERTIFICATE_NUMBER)                .uuid(UUID.fromString(UUID_NUMBER))                .build();        ObjectMapper mapper = new ObjectMapper();        var dto = mapper.writeValueAsString(requestMessage);        var message = MessageBuilder                .withPayload(dto)                .build();        var captchaMessageRequestDto = CaptchaMessageRequestDto.builder()                .uuid(UUID.fromString(UUID_NUMBER))                .build();        given(scrapService.scrapCaptcha(requestMessage, null))                .willReturn(captchaMessageRequestDto);        messageService.receiveMessageFromQueueWithCarData(message);        verify(gateway, times(1)).sendCaptchaRequestToUser(Mockito.any(Message.class));    }    @Test    @DisplayName("отправление запроса c captcha в очередь (из парсера боту)")    void shouldSendCaptchaRequestToQueue() {        var captchaMessageRequestDto = CaptchaMessageRequestDto.builder()                .uuid(UUID.fromString(UUID_NUMBER))                .build();        messageService.sendCaptchaRequestToUser(captchaMessageRequestDto);        verify(gateway, times(1)).sendCaptchaRequestToUser(Mockito.any(Message.class));    }//    @Test//    @DisplayName("получение запроса c данными о ТС из очереди (парсером)")//    void shouldReceiveMessageFromQueueWithCaptcha() throws IOException {//        var chat = ru.arkasandr.carfinesearcher.model.Chat.builder()//                .chatId(ID)//                .build();//        var photo = "photo".getBytes();//        var captchaMessageRequestDto = CaptchaMessageRequestDto.builder()//                .uuid(UUID.fromString(UUID_NUMBER))//                .build();//        ObjectMapper mapper = new ObjectMapper();//        var dto = mapper.writeValueAsString(captchaMessageRequestDto);//        var message = MessageBuilder//                .withPayload(dto)//                .build();////        given(chatRepository.findChatByRequestUUID(UUID.fromString(UUID_NUMBER)))//                .willReturn(of(chat));//        given(createPhotoResource(photo, "photo", ".png"))//                .willReturn(new ByteArrayResource(photo));//       // given(restTemplate.exchange())////        messageService.receiveMessageFromQueueWithCaptcha(message);////      //  verify(restTemplate, times(1)).exchange(Mockito.any(Message.class));//    }    @Test    @DisplayName("отправление запроса cо значением captcha в очередь (парсеру)")    void shouldSendCaptchaValueRequestToQueue() {        Car car = Car.builder()                .id(ID)                .registrationNumber(REGISTRATION_NUMBER)                .certificateNumber(CERTIFICATE_NUMBER)                .build();        GibddRequest request = GibddRequest.builder()                .id(UUID.fromString(UUID_NUMBER))                .createDate(now())                .requestDate(now())                .car(car)                .build();        car.setRequest(Set.of(request));        messageService.sendMessageToQueueWithCaptchaValue(car, CAPTCHA);        assertThat(request.getStatus())                .isEqualTo(RequestStatus.CAPTCHA_SENT);        verify(requestRepository, times(1)).save(Mockito.any(GibddRequest.class));    }    @Test    @DisplayName("получение запроса cо значением captcha из очереди (парсером)")    void shouldReceiveCaptchaValueFromQueue() throws JsonProcessingException {        var requestMessage = CaptchaMessageResponseDto.builder()                .uuid(UUID.fromString(UUID_NUMBER))                .value(CAPTCHA)                .build();        ObjectMapper mapper = new ObjectMapper();        var dto = mapper.writeValueAsString(requestMessage);        var message = MessageBuilder                .withPayload(dto)                .build();        var answer = Answer.builder()                .checkingMessage(null)                .build();        given(scrapService.scrapFines(requestMessage))                .willReturn(answer);        messageService.receiveMessageFromQueueWithCaptchaValue(message);        verify(gateway, times(1)).sendAnswerToUser(Mockito.any(Message.class));    }    @Test    @DisplayName("отправление запроса cо штрафами в очередь (из парсера боту)")    void shouldSendUserFinesToQueue() {        var answer = Answer.builder()                .uuid(UUID.fromString(UUID_NUMBER))                .build();        messageService.sendFinesToUser(answer);        verify(gateway, times(1)).sendAnswerToUser(Mockito.any(Message.class));    }}