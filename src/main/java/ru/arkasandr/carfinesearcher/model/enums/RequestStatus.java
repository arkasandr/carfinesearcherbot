package ru.arkasandr.carfinesearcher.model.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    READY_FOR_SEND(),  //ГОТОВ К ОТПРАВКЕ
    SENDING(),  // ОТПРАВЛЯЕТСЯ
    EXECUTED(),  //ВЫПОЛНЕН
    REJECTED(),  //ОТКЛОНЕН
    CAPTCHA_IS_WAITING(), //ОЖИДАЕТСЯ CAPTCHA
    CAPTCHA_SENT(),  //CAPTCHA ОТПРАВЛЕНА
    CAPTCHA_ERROR(),  //НЕВЕРНАЯ CAPTCHA
    DELETED();   //УДАЛЕН
}
