package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.repository.CarRepository;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    @Transactional(rollbackFor = Exception.class)
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Transactional(readOnly = true)
    public Car findCarByRegistrationNumber(String registrationNumber) {
        return carRepository.findByRegistrationNumber(registrationNumber)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Car findCarByChatIdAndCertificateNumberIsNull(Long id) {
        return carRepository.findCarByChatIdAndCertificateNumberIsNull(id)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Set<Long> findCarIdsWithFullData(Long id) {
        return carRepository.findCarIdsWithFullData(id);
    }
}
