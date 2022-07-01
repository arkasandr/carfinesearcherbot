package ru.arkaleks.carfinesearcher.telegram.constants;

public enum BotMessageEnum {

    //ответы на команды с клавиатуры

    CHOOSE_METHOD_MESSAGE("Выберите способ загрузки\uD83D\uDC47 "),
    REDACT_MENU_MESSAGE("Меню редактора"),

    EXCEPTION_ILLEGAL_MESSAGE("Нет, к такому меня не готовили! Я работаю или с текстом, или с файлом"),
    EXCEPTION_WHAT_THE_FUCK("Что-то пошло не так. Обратитесь к программисту"),
    HELP_MESSAGE("Do you need help?");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
