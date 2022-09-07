package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.model.Chat;
import ru.arkasandr.carfinesearcher.model.GibddRequest;
import ru.arkasandr.carfinesearcher.repository.ChatRepository;
import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;

import static java.time.LocalDateTime.now;
import static ru.arkasandr.carfinesearcher.model.enums.RequestStatus.READY_FOR_SEND;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final CarService carService;

    @Transactional
    public Chat saveChatFromMessage(Message message) {
        var chat = Chat.builder()
                .chatId(message.getChatId())
                .firstName(message.getChat().getFirstName())
                .lastName(message.getChat().getLastName())
                .userName(message.getChat().getUserName())
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
}
