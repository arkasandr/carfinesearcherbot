package ru.arkasandr.carfinesearcher.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.arkasandr.carfinesearcher.model.Car;
import ru.arkasandr.carfinesearcher.model.Chat;
import ru.arkasandr.carfinesearcher.repository.ChatRepository;

import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final CarService carService;

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
                .orElse(null);
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveRegistrationNumber(Chat chat, String registrationNumber) {
        var car = carService.findCarByRegistrationNumber(registrationNumber);
        if (isNull(car)) {
            car = Car.builder()
                    .registrationNumber(registrationNumber)
                    .build();
        }
        car.setChat(chat);
        carService.save(car);
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveCertificateNumber(Chat chat, String certificateNumber) {
        var car = carService.findCarByChatIdAndCertificateNumberIsNull(chat.getId(), certificateNumber);
        if (isNull(car)) {
            car = Car.builder()
                    .certificateNumber(certificateNumber)
                    .build();
        } else {
            car.setCertificateNumber(certificateNumber);
        }
        car.setChat(chat);
        carService.save(car);
    }
}
