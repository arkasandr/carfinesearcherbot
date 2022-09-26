package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.config.props.GibddProperties;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.model.Chat;
import ru.arkasandr.carfinesearcher.repository.ChatRepository;

import javax.persistence.EntityNotFoundException;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final CarService carService;
    private final GibddProperties gibddProperties;

    @Transactional
    public Chat saveChatFromMessage(Message message) {
        var chat = Chat.builder()
                .chatId(message.getChatId())
                .firstName(message.getChat().getFirstName())
                .lastName(message.getChat().getLastName())
                .userName(message.getChat().getUserName())
                .maxRequestAttempt(gibddProperties.getMaxDailyRequestAttempt())
                .build();
        return chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public Chat findChatByChatId(String chatId) {
        return chatRepository.findChatByChatId(Long.valueOf(chatId))
                .orElse(Chat.builder().build());
    }

    @Transactional
    public void saveRegistrationNumber(Chat chat, String registrationNumber) {
        var car = Car.builder()
                .registrationNumber(registrationNumber)
                .updateDate(now())
                .chat(chat)
                .build();
        carService.save(car);
    }

    @Transactional
    public void saveCertificateNumber(Chat chat, Car car, String certificateNumber) {
        car.setCertificateNumber(certificateNumber);
        car.setUpdateDate(now());
        car.setChat(chat);
        carService.save(car);
    }

    @Transactional
    public Chat changeChatMaxRequestAttempt(Chat chat) {
        if (chat.getChatId() != null) {
            var existChat = chatRepository.findChatByChatId(chat.getChatId())
                    .orElseThrow(() -> new EntityNotFoundException("Запись о чате с id = " + chat.getChatId() + " отсутствует!"));
            existChat.setMaxRequestAttempt(chat.getMaxRequestAttempt());
            return chatRepository.save(existChat);
        }
        return Chat.builder().build();
    }
}
