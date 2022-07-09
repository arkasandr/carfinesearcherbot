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

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestProcessService {

    private final CarRepository carRepository;
    private final GibddRequestRepository requestRepository;


    @Transactional(rollbackFor = Exception.class)
    public GibddRequest sendRequest(Long id) {
        var newRequest = GibddRequest.builder()
                .requestDate(now())
                .status(RequestStatus.SENDING)
                .build();
        var existCar = carRepository.findById(id).orElse(null);
        if (!isNull(existCar)) {
            existCar.setRequest(newRequest);
        }
        return requestRepository.save(newRequest);
    }
}
