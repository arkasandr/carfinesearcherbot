package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.GibddRequest;
import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;
import ru.arkasandr.carfinesearcher.service.message.MessageService;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.toLong;
import static ru.arkasandr.carfinesearcher.model.enums.RequestStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {

    private static final Integer ONE_ATTEMPT = 1;

    private final CarService carService;
    private final GibddRequestRepository requestRepository;
    private final MessageService messageService;

    @Value("${gibdd.maxCaptchaAttempt}")
    Integer maxCaptchaAttempt;

    @Transactional
    public GibddRequest saveReadyForSendRequest(String chatId) {
        var existCar = carService.findCarWithLastUpdateDateByChatId(toLong(chatId))
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с chatId = " + chatId + " отсутствует!"));
        var request = GibddRequest.builder()
                .createDate(now())
                .status(READY_FOR_SEND)
                .car(existCar)
                .build();
        requestRepository.save(request);
        var carRequests = existCar.getRequest();
        carRequests.add(request);
        existCar.setRequest(carRequests);
        carService.save(existCar);
        return request;
    }

    @Transactional
    public GibddRequest changeRequestStatusForSending(GibddRequest request) {
        request.setStatus(SENDING);
        request.setRequestDate(now());
        return requestRepository.save(request);
    }

    @Transactional
    public void sendRequestWithCarDataToParser(Long carId) {
        var sendingRequest = requestRepository.findReadyForSendRequestByCarId(carId)
                .orElseThrow(() -> new EntityNotFoundException("Запрос с carId = " + carId + " отсутствует!"));
        var existCar = carService.findCarWithRequestById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + carId + " отсутствует!"));
        messageService.sendMessageToQueueWithCarData(existCar, sendingRequest);
        changeRequestStatusForSending(sendingRequest);
    }

    @Transactional
    public GibddRequest sendRequestWithCaptchaValueToParser(Long id, String captcha) {
        var request = new GibddRequest();
        var existCar = carService.findCarWithRequestById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + id + " отсутствует!"));
        if (!isNull(existCar.getRequest())) {
            var existRequest = existCar.getRequest().stream()
                    .filter(r -> nonNull(r.getCreateDate()))
                    .max(Comparator.comparing(GibddRequest::getRequestDate))
                    .orElseThrow(() -> new IllegalArgumentException("Запрос должен быть найден!"));
            existRequest.setRequestDate(now());
            existRequest.setStatus(SENDING);
            request = requestRepository.save(existRequest);
        }
        messageService.sendMessageToQueueWithCaptchaValue(request, captcha);
        changeRequestStatusForCaptchaSend(request, captcha);
        return request;
    }

    @Transactional
    public GibddRequest changeRequestStatusForCaptchaSend(GibddRequest request, String captcha) {
        request.setStatus(CAPTCHA_SENT);
        request.setCaptchaCode(toLong(captcha));
        return requestRepository.save(request);
    }

    @Transactional
    public GibddRequest increaseCaptchaAttempt(Long id) {
        var result = new GibddRequest();
        var existCar = carService.findCarWithRequestById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + id + " отсутствует!"));
        if (!isNull(existCar.getRequest())) {
            var existRequest = existCar.getRequest().stream()
                    .max(Comparator.comparing(GibddRequest::getRequestDate))
                    .orElseThrow(() -> new IllegalArgumentException("Запрос должен быть найден!"));
            if (isNull(existRequest.getCaptchaAttempt())) {
                existRequest.setCaptchaAttempt(1);
            } else {
                existRequest.setCaptchaAttempt(existRequest.getCaptchaAttempt() + ONE_ATTEMPT);
            }
            result = requestRepository.save(existRequest);
        }
        return result;
    }

    @Transactional
    public boolean isDailyRequestsLimit(Long chatId) {
        return requestRepository.isDailyRequestsLimit(chatId);
    }

    @Transactional
    public boolean isCurrentRequestsLimit(Long chatId) {
        return requestRepository.isCurrentRequestsLimit(chatId);
    }

    @Transactional
    public GibddRequest findByChatId(String chatId) {
        return requestRepository.findByChatId(toLong(chatId))
                .orElseThrow(() -> new EntityNotFoundException("Запрос с chatId = " + chatId + " отсутствует!"));
    }
}
