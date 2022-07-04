package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.repository.CarRepository;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    @Transactional
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public Car findCarByRegistrationNumber(String registrationNumber) {
        return carRepository.findByRegistrationNumber(registrationNumber)
                .orElse(null);
    }

    @Transactional
    public Car findCarByChatIdAndCertificateNumberIsNull(Long chatId, String certificateNumber) {
        return carRepository.findCarByChatIdAndCertificateNumberIsNull(chatId)
                .orElse(null);
    }
}
