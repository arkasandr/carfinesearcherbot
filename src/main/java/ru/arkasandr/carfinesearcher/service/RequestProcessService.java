package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.GibddRequest;
import ru.arkasandr.carfinesearcher.model.enums.RequestStatus;
import ru.arkasandr.carfinesearcher.repository.CarRepository;
import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;

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


    @Transactional(rollbackFor = Exception.class)
    public void sendRequest(Long id) {
        var existCar = carRepository.findById(id).orElse(null);
        if (!isNull(existCar) && !isNull(existCar.getRequest())) {
            var existRequest = existCar.getRequest();
            existRequest.setRequestDate(now());
            existRequest.setStatus(SENDING);
            requestRepository.save(existRequest);
        }
    }
}
