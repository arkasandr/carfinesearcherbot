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

    @Query(nativeQuery = true, value = " select * from car c " +
            " left join chat ch on c.chat_id = ch.id " +
            " where c.chat_id = :chatId " +
            " and c.certificate_number is null")
    Optional<Car> findCarByChatIdAndCertificateNumberIsNull(@Param("chatId") Long chatId);
}
