package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.model.GibddRequest;
import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;
import ru.arkasandr.carfinesearcher.service.message.MessageService;

import javax.persistence.EntityNotFoundException;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static ru.arkasandr.carfinesearcher.model.enums.RequestStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestProcessService {

    private final CarService carService;
    private final GibddRequestRepository requestRepository;
    private final MessageService messageService;


    @Transactional
    public GibddRequest sendRequestToGibddWithCarData(Long id) {
        var result = new GibddRequest();
        Car existCar = carService.findCarWithRequestById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + id + " отсутствует!"));
        if (!isNull(existCar.getRequest())) {
            var existRequest = existCar.getRequest().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Запрос должен быть найден!"));
            existRequest.setRequestDate(now());
            existRequest.setStatus(SENDING);
            result = requestRepository.save(existRequest);
        }
        messageService.sendMessageToQueueWithCarData(existCar);
        return result;
    }

    @Transactional
    public GibddRequest sendRequestToGibddWithCaptchaValue(Long id, String captcha) {
        var result = new GibddRequest();
        Car existCar = carService.findCarWithRequestById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + id + " отсутствует!"));
        if (!isNull(existCar.getRequest())) {
            var existRequest = existCar.getRequest().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Запрос должен быть найден!"));
            existRequest.setRequestDate(now());
            existRequest.setStatus(SENDING);
            result = requestRepository.save(existRequest);
        }
        messageService.sendMessageToQueueWithCaptchaValue(existCar, captcha);
        return result;
    }
}
