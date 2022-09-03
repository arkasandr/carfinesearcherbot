package ru.arkasandr.carfinesearcher.telegram.constants;

public enum BotMessageEnum {


    START_MESSAGE("Введите и отправьте регистрационный знак ТС в формате: А000АА777 или А000АА77, а затем введите и отправьте свидетельство о регистрации ТС в формате: 99АА999999 или или 9988999999"),
    SUCCESS_DATA_SENDING("Запрос успешно отправлен! Ожидайте"),
    HELP_MESSAGE("Нужно придумать хэлп"),
    REGISTRATION_NUMBER_MESSAGE("Регистрационный знак принят. Введите и отправьте свидетельство о регистрации ТС в формате: 99АА999999 или 9988999999"),
    CERTIFICATE_NUMBER_MESSAGE("Cвидетельство о регистрации принято. Для продолжения нажмите \"Отправить запрос\""),
    CAPTCHA_VALUE_MESSAGE("Проверочный код передан. Ожидайте"),
    WRONG_REGISTRATION_NUMBER_MESSAGE("Неверный ввод! Необходимый формат: А000АА777 или А000АА77"),
    WRONG_CERTIFICATE_NUMBER_MESSAGE("Неверный ввод! Необходимый формат: 99АА999999 или 9988999999"),
    WRONG_CAPTCHA_VALUE_MESSAGE("Неверный ввод! Необходимый формат: 12345"),
    READY_DATA_MESSAGE("Все данные приняты. Для продолжения нажмите \"Отправить запрос\""),

    EXCEPTION_ILLEGAL_MESSAGE("Internal Error"),
    EXCEPTION_WHAT_THE_FUCK("Что-то пошло не так. Обратитесь к разработчику."),
    EXCEPTION_EMPTY_MESSAGE("Вы отправили пустое сообщение!"),
    EXCEPTION_WRONG_MESSAGE("Вы отправили некорректные данные! Попробуйте еще раз!"),
    EXCEPTION_WRONG_RUSSIAN_LANGUAGE_MESSAGE("Сообщение должно содержать только буквы русского алфавита! Попробуйте еще раз!"),
    EXCEPTION_WRONG_ENGLISH_LANGUAGE_MESSAGE("Сообщение должно содержать только буквы латинского алфавита! Попробуйте еще раз!"),
    EXCEPTION_CERTIFICATE_BEFORE_REGISTRATION("Сначала введите регистрационный знак ТС!"),
    EXCEPTION_EXISTING_REGISTRATION_NUMBER("Регистрационный знак уже сохранен. Теперь введите свидетельство о регистрации."),
    EXCEPTION_CAPTCHA_WAITING_REQUEST("Неверный ввод! Возможно, Вы хотели ввести проверочный код, но у Вас нет незаконченных запросов."),
    EXCEPTION_EXISTING_REQUEST("Имеется незаконченный запрос! Введите свидетельство о регистрации!"),
    EXCEPTION_CAPTCHA_REPEAT("Неверно введен проверочный код! Попробуйте еще раз!"),
    EXCEPTION_CAPTCHA_MAX_ATTEMPT("Превышено количество неверно введенных проверочных кодов! Текущий запрос отклонен!");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
