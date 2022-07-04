package ru.arkasandr.carfinesearcher.telegram.constants;

public enum BotMessageEnum {


    START_MESSAGE("Введите и отправьте регистрационный знак ТС в формате: А000АА777, а затем введите и отправьте свидетельство о регистрации ТС в формате: 99АА999999"),
    SUCCESS_DATA_SENDING("Запрос успешно отправлен!"),
    HELP_MESSAGE("Нужно придумать хэлп"),
    REGISTRATION_NUMBER_MESSAGE("Регистрационный знак  принят. Введите и отправьте свидетельство о регистрации ТС в формате: 99АА999999"),
    CERTIFICATE_NUMBER_MESSAGE("Cвидетельство о регистрации  принято. Для продолжения нажмите \"Отправить запрос\""),
    WRONG_REGISTRATION_NUMBER_MESSAGE("Регистрационный знак ТС введен неверно. Необходимый формат: А000АА777"),

    EXCEPTION_ILLEGAL_MESSAGE("Умею читать только текст"),
    EXCEPTION_WHAT_THE_FUCK("Что-то пошло не так. Обратитесь к разработчику"),
    EXCEPTION_EMPTY_MESSAGE("Вы отправили пустое сообщение!"),
    EXCEPTION_WRONG_MESSAGE("Вы отправили некорректные данные! Попробуйте еще раз!");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
