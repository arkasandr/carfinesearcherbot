package ru.arkasandr.carfinesearcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.arkasandr.carfinesearcher.model.GibddRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GibddRequestRepository extends JpaRepository<GibddRequest, UUID> {

    @Query(nativeQuery = true, value = " select "
            + " (select ch.max_request_attempt from chat ch "
            + " where ch.chat_id = :chatId) "
            + " >= "
            + " (select count(*) from gibdd_request r "
            + " left join car c on r.car_id = c.id "
            + " left join chat ch on c.chat_id = ch.id "
            + " where ch.chat_id = :chatId "
            + " and r.request_date between (NOW() - INTERVAL '1 DAY') and NOW())")
    boolean isDailyRequestsLimit(@Param("chatId") Long chatId);

    @Query(value =
            " select CASE WHEN count(r.id) > 0 THEN true ELSE false END from GibddRequest r "
                    + " left join Car c on c.id = r.car.id "
                    + " left join Chat ch on ch.id = c.chat.id "
                    + " where ch.chatId = :chatId "
                    + " and c.updateDate = (SELECT MAX(c.updateDate) from c) "
                    + " and (r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).READY_FOR_SEND}  "
                    + " or r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).SENDING} "
                    + " or r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).CAPTCHA_IS_WAITING} "
                    + " or r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).CAPTCHA_SENT} "
                    + " or r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).CAPTCHA_ERROR}) ")
    boolean isCurrentRequestsLimit(@Param("chatId") Long chatId);

    @Query(value =
            " select CASE WHEN count(r.id) > 0 THEN true ELSE false END from GibddRequest r "
                    + " left join Car c on c.id = r.car.id "
                    + " left join Chat ch on ch.id = c.chat.id "
                    + " where ch.chatId = :chatId "
                    + " and c.updateDate = (SELECT MAX(c.updateDate) from c) "
                    + " and r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).READY_FOR_SEND} ")
    boolean isCurrentSendingLimit(@Param("chatId") Long chatId);

    @Query(value = " select r from GibddRequest r "
            + " left join Car c on c.id = r.car.id "
            + " where c.updateDate = (SELECT MAX(c.updateDate) from c) "
            + " and r.status = ?#{T(ru.arkasandr.carfinesearcher.model.enums.RequestStatus).READY_FOR_SEND} "
            + " and c.id = :carId ")
    Optional<GibddRequest> findReadyForSendRequestByCarId(@Param("carId") Long carId);

    @Query(value = " select r from GibddRequest r "
            + " left join Car c on c.id = r.car.id "
            + " left join Chat ch on ch.id = c.chat.id "
            + " where c.updateDate = (SELECT MAX(c.updateDate) from c) "
            + " and ch.chatId = :chatId ")
    Optional<GibddRequest> findByChatId(@Param("chatId") Long chatId);

    @Modifying
    @Query(nativeQuery = true, value = " update gibdd_request "
            + " set status = 'DELETED' "
            + " where (status = 'READY_FOR_SEND' "
            + " or status = 'CAPTCHA_IS_WAITING' "
            + " or status = 'SENDING' "
            + " or status = 'CAPTCHA_SENT') "
            + " and create_date < NOW() - 1 * INTERVAL '1 hour'")
    void archiveDeadRequests();

    @Query(nativeQuery = true, value = " select * from gibdd_request "
            + " where (status = 'READY_FOR_SEND' "
            + " or status = 'CAPTCHA_IS_WAITING' "
            + " or status = 'SENDING' "
            + " or status = 'CAPTCHA_SENT') "
            + " and create_date < NOW() - 1 * INTERVAL '1 hour'")
    Collection<GibddRequest> findDeadRequestsIds();
}
