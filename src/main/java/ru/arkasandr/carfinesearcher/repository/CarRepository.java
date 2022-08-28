package ru.arkasandr.carfinesearcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.arkasandr.carfinesearcher.model.Car;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByRegistrationNumber(String registrationNumber);

    @Query(nativeQuery = true, value = " select * from car c "
            + " left join chat ch on c.chat_id = ch.id "
            + " where c.chat_id = :id "
            + " and c.certificate_number is null")
    Optional<Car> findCarByChatIdAndCertificateNumberIsNull(@Param("id") Long id);

    @Query(value = " select c.id from Car c "
            + " left join c.chat ch "
            + " left join c.request r "
            + " where ch.id = :id "
            + " and c.registrationNumber is not null "
            + " and c.certificateNumber is not null "
            + " and r.status != ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).SENDING}")
    Long findCarIdWithFullDataAndNotInSendingStatus(@Param("id") Long id);

    @Query(value = " select c.id from Car c "
            + " left join c.chat ch "
            + " left join c.request r "
            + " where ch.id = :id "
            + " and c.registrationNumber is not null "
            + " and c.certificateNumber is not null "
            + " and r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).CAPTCHA_IS_WAITING}")
    Long findCarIdWithFullDataAndCaptchaIsWaitingStatus(@Param("id") Long id);

    @Query(nativeQuery = true, value = " select * from car c "
            + " where c.certificate_number is null")
    Optional<Car> findCarWithoutCertificateNumber();

    @Query(value = " select c from Car c "
            + " left join fetch c.request "
            + " where c.id = :id ")
    Optional<Car> findCarWithRequestById(@Param("id") Long id);

    @Query(value = " select c.id from Car c "
            + " left join c.chat ch "
            + " left join c.request r "
            + " where ch.id = :id "
            + " and c.registrationNumber is not null "
            + " and c.certificateNumber is not null "
            + " and r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).READY_FOR_SEND}")
    Long findCarIdWithFullDataAndReadyForSend(@Param("id") Long id);
}
