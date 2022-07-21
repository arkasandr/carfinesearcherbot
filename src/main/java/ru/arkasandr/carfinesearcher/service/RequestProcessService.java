package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.model.GibddRequest;
import ru.arkasandr.carfinesearcher.model.enums.RequestStatus;
import ru.arkasandr.carfinesearcher.repository.CarRepository;
import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;
import ru.arkasandr.carfinesearcher.service.message.MessageService;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static ru.arkasandr.carfinesearcher.model.enums.RequestStatus.SENDING;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestProcessService {

    private final CarRepository carRepository;
    private final GibddRequestRepository requestRepository;
    private final MessageService messageService;


    @Transactional(rollbackFor = Exception.class)
    public GibddRequest sendRequest(Long id) {
        var result = new GibddRequest();
        Car existCar = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + id + " отсутствует!"));
        if (!isNull(existCar.getRequest())) {
            var existRequest = existCar.getRequest();
            existRequest.setRequestDate(now());
            existRequest.setStatus(SENDING);
            result = requestRepository.save(existRequest);
            messageService.sendMessageToQueue(result.getId());

        }
        return result;
    }
}
