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
    public Long findCarIdWithFullDataAndReadyForSend(Long id) {
        return carRepository.findCarIdByFullDataAndReadyForSend(id);
    }

    @Transactional(readOnly = true)
    public Long findCarIdWithFullDataAndCaptchaIsWaitingStatus(Long id) {
        return carRepository.findCarIdWithFullDataAndCaptchaIsWaitingStatus(id);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarWithoutCertificateNumber() {
        return carRepository.findCarWithoutCertificateNumber();
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarWithRequestById(Long id) {
        return carRepository.findCarWithRequestById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarByCertificateNumberAndLastUpdateDate(Long id, String certificateNumber) {
        return carRepository.findCarByCertificateNumberAndLastUpdateDate(id, certificateNumber);
    }

    @Transactional(readOnly = true)
    public Long findCarIdWithFullDataAndLastUpdateDate(Long id) {
        return carRepository.findCarIdWithFullDataAndLastUpdateDate(id);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarWithRegistrationNumberAndLastUpdateDate(Long id) {
        return carRepository.findCarWithRegistrationNumberAndLastUpdateDate(id);
    }

    @Transactional(readOnly = true)
    public Optional<Car> findCarWithLastUpdateDateByChatId(Long chatId) {
        return carRepository.findCarWithLastUpdateDateByChatId(chatId);
    }
}
