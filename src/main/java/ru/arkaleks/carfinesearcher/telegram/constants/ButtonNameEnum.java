package ru.arkaleks.carfinesearcher.telegram.constants;

public enum ButtonNameEnum {

    SENT_NUMBER_BUTTON("Отправить на проверку"),

    REDACT_DATA_BUTTON("Редактировать данные"),

    GENERATE_REPORT_BUTTON("Сформировать отчёт"),

    HELP_BUTTON("Помощь");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
