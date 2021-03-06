package ru.arkasandr.carfinesearcher.telegram.constants;

public enum ButtonNameEnum {

    SENT_BUTTON("Отправить запрос"),

    HELP_BUTTON("Помощь");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
