package ru.arkasandr.carfinesearcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.arkasandr.carfinesearcher.model.GibddRequest;

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
}
