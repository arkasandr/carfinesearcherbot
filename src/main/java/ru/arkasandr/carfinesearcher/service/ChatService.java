package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.model.Chat;
import ru.arkasandr.carfinesearcher.model.GibddRequest;
import ru.arkasandr.carfinesearcher.model.enums.RequestStatus;
import ru.arkasandr.carfinesearcher.repository.ChatRepository;
import ru.arkasandr.carfinesearcher.repository.GibddRequestRepository;

import javax.persistence.EntityNotFoundException;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final CarService carService;
    private final GibddRequestRepository gibddRequestRepository;

    @Transactional(rollbackFor = Exception.class)
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
                .orElseThrow(() -> new EntityNotFoundException("Запись о ТС с id = " + chatId + " отсутствует!"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRegistrationNumber(Chat chat, String registrationNumber) {
        var car = Car.builder()
                .registrationNumber(registrationNumber)
                .chat(chat)
                .build();
        carService.save(car);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveCertificateNumber(Chat chat, Car car, String certificateNumber) {
        car.setCertificateNumber(certificateNumber);
        car.setChat(chat);
        carService.save(car);
        var newRequest = GibddRequest.builder()
                .createDate(now())
                .status(RequestStatus.READY_FOR_SEND)
                .car(car)
                .build();
        gibddRequestRepository.save(newRequest);
    }
}
