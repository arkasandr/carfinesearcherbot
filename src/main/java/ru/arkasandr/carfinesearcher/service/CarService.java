package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.repository.CarRepository;

import java.util.Optional;

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
    public Optional<Car> findCarByRegistrationNumber(String registrationNumber) {
        return carRepository.findByRegistrationNumber(registrationNumber);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarByChatIdAndCertificateNumberIsNull(Long id) {
        return carRepository.findCarByChatIdAndCertificateNumberIsNull(id);
    }

    @Transactional(readOnly = true)
    public Long findCarIdWithFullDataAndNotInSendingStatus(Long id) {
        return carRepository.findCarIdWithFullDataAndNotInSendingStatus(id);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarWithoutCertificateNumber() {
        return carRepository.findCarWithoutCertificateNumber();
    }
}
