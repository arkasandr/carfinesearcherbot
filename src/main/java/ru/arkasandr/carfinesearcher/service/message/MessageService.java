package ru.arkasandr.carfinesearcher.service.message;import com.fasterxml.jackson.core.JsonProcessingException;import com.fasterxml.jackson.databind.ObjectMapper;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.apache.commons.io.FileUtils;import org.springframework.beans.factory.annotation.Value;import org.springframework.core.io.ByteArrayResource;import org.springframework.http.HttpEntity;import org.springframework.http.HttpHeaders;import org.springframework.http.HttpMethod;import org.springframework.http.MediaType;import org.springframework.messaging.Message;import org.springframework.messaging.support.MessageBuilder;import org.springframework.stereotype.Service;import org.springframework.util.LinkedMultiValueMap;import org.springframework.web.client.RestTemplate;import ru.arkasandr.carfinesearcher.exception.TelegramFileUploadException;import ru.arkasandr.carfinesearcher.model.Car;import ru.arkasandr.carfinesearcher.service.GibddScrapService;import ru.arkasandr.carfinesearcher.service.message.dto.CaptchaMessageRequest;import ru.arkasandr.carfinesearcher.service.message.dto.GibddRequestMessageDto;import java.io.File;import java.io.IOException;import java.nio.file.Files;import java.nio.file.Path;import java.text.MessageFormat;@RequiredArgsConstructor@Service@Slf4jpublic class MessageService {    @Value("${telegram.token}")    String botToken;    @Value("${telegram.url}")    String url;    private final MessageGateway gateway;    private final GibddScrapService scrapService;    private final RestTemplate restTemplate;    /**     * Метод отправляет запрос c данными о ТС в очередь для передачи парсеру (выполняется на стороне бота)     */    public void sendMessageToQueueWithCarData(Car car) {        var request = car.getRequest().stream()                .findFirst()                .orElseThrow(() -> new IllegalArgumentException("Запрос должен быть найден!"));        var dto = GibddRequestMessageDto.builder()                .registrationNumber(car.getRegistrationNumber())                .certificateNumber(car.getCertificateNumber())                .uuid(request.getId())                .build();        var message = MessageBuilder                .withPayload(dto)                .build();        gateway.sendGibddRequestWithCarData(message);        log.info("Request message with UUID = {} was sent to queue", request.getId());    }    /**     * Метод получает данные о ТС для парсера из очереди (выполняется на стороне парсера)     */    public void receiveMessageFromQueueWithCarData(Message<String> message) {        String messagePayload = message.getPayload();        ObjectMapper mapper = new ObjectMapper();        try {            GibddRequestMessageDto dto = mapper.readValue(messagePayload, GibddRequestMessageDto.class);            log.info("Response message with UUID = {} was received from queue", dto.getUuid().toString());            var captchaMessageRequest = scrapService.getCaptcha(dto);            sendCaptchaRequestToUser(captchaMessageRequest);            log.info("Response message with UUID = {} with captcha was send to user", captchaMessageRequest.getUuid().toString());        } catch (JsonProcessingException e) {            e.printStackTrace();        }    }    /**     * Метод отправляет запрос в очередь c captcha для распознования пользователем (выполняется на стороне парсера)     */    public void sendCaptchaRequestToUser(CaptchaMessageRequest captchaMessageRequest) {        var message = MessageBuilder                .withPayload(captchaMessageRequest)                .build();        gateway.sendCaptchaRequestToUser(message);        log.info("Request message with UUID = {} was sent to queue", captchaMessageRequest.getUuid());    }    /**     * Метод получает запрос из очереди c captcha для распознования пользователем (выполняется на стороне бота)     */    public void receiveMessageFromQueueWithCaptcha(Message<String> message) {        String messagePayload = message.getPayload();        ObjectMapper mapper = new ObjectMapper();        try {            CaptchaMessageRequest dto = mapper.readValue(messagePayload, CaptchaMessageRequest.class);            log.info("Response message with UUID = {} was received from queue", dto.getUuid().toString());            byte[] content = dto.getCaptcha();            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();            map.add("photo", createPhotoResource(content, "captcha", ".png"));            HttpHeaders headers = new HttpHeaders();            headers.setContentType(MediaType.MULTIPART_FORM_DATA);            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);            restTemplate.exchange(                    "https://api.telegram.org/bot5474369597:AAFH48Nx4VXbs5x64fDb-2fWeSGiIWo2AnM/sendPhoto?chat_id=436653019",                    HttpMethod.POST,                    requestEntity,                    String.class);        } catch (Exception e) {            throw new TelegramFileUploadException();        }    }    public static ByteArrayResource createPhotoResource(byte[] photo, String name, String suffix)            throws IOException {        return new ByteArrayResource(Files.readAllBytes(createOfficeDocumentFile(photo, name, suffix))) {            @Override            public String getFilename() {                return MessageFormat.format("{0}.{1}", name, suffix);            }        };    }    private static Path createOfficeDocumentFile(byte[] photo, String name, String suffix) throws IOException {        File file = File.createTempFile(name, suffix);        FileUtils.writeByteArrayToFile(file, photo);        return file.toPath();    }}