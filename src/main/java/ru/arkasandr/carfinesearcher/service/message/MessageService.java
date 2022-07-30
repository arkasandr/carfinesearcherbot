package ru.arkasandr.carfinesearcher.service.message;import com.fasterxml.jackson.core.JsonProcessingException;import com.fasterxml.jackson.databind.ObjectMapper;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.messaging.Message;import org.springframework.messaging.support.MessageBuilder;import org.springframework.stereotype.Service;import ru.arkasandr.carfinesearcher.service.message.dto.CaptchaMessageRequest;import ru.arkasandr.carfinesearcher.model.Car;import ru.arkasandr.carfinesearcher.service.GibddScrapService;import ru.arkasandr.carfinesearcher.service.message.dto.GibddRequestMessageDto;@RequiredArgsConstructor@Service@Slf4jpublic class MessageService {    private final MessageGateway gateway;    private final GibddScrapService scrapService;    /**     * Метод отправляет запрос c данными о ТС в очередь для передачи парсеру     */    public void sendMessageToQueueWithCarData(Car car) {        var request = car.getRequest().stream()                .findFirst()                .orElseThrow(() -> new IllegalArgumentException("Запрос должен быть найден!"));        var dto = GibddRequestMessageDto.builder()                .registrationNumber(car.getRegistrationNumber())                .certificateNumber(car.getCertificateNumber())                .uuid(request.getId())                .build();        var message = MessageBuilder                .withPayload(dto)                .build();        gateway.sendGibddRequestWithCarData(message);        log.info("Request message with UUID = {} was sent to queue", request.getId());    }    /**     * Метод получает данные о ТС для парсера из очереди     */    public void receiveMessageFromQueueWithCarData(Message<String> message) {        String messagePayload = message.getPayload();        ObjectMapper mapper = new ObjectMapper();        try {            GibddRequestMessageDto dto = mapper.readValue(messagePayload, GibddRequestMessageDto.class);            log.info("Response message with UUID = {} was received from queue", dto.getUuid().toString());            CaptchaMessageRequest captchaMessageRequest = scrapService.getCaptcha(dto);            log.info("Response message with UUID = {}is ready for sending to queue", captchaMessageRequest.getUuid().toString());        } catch (JsonProcessingException e) {            e.printStackTrace();        }    }}