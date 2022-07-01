package ru.arkaleks.carfinesearcher.telegram.constants;

public enum BotMessageEnum {

    EXCEPTION_ILLEGAL_MESSAGE("Умею читать только текст"),
    EXCEPTION_WHAT_THE_FUCK("Что-то пошло не так. Обратитесь к разработчику"),
    HELP_MESSAGE("Введите регистрационный знак и свидетельство о регистрации ТС в формате: А000АА777, 99АА999999"),
    SUCCESS_DATA_SENDING("Запрос успешно отправлен!");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
