package ru.arkasandr.carfinesearcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.arkasandr.carfinesearcher.model.Chat;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(nativeQuery = true, value = " select * from chat ch "
            + " where ch.chat_id = :chatId ")
    Optional<Chat> findChatByChatId(@Param("chatId") Long chatId);

    @Query(nativeQuery = true, value = " select * from chat ch "
            + " left join car c on ch.id = c.chat_id "
            + " left join gibdd_request gr on c.id = gr.car_id "
            + " where gr.id = :uuid ")
    Optional<Chat> findChatByRequestUUID(@Param("uuid") UUID uuid);
}
