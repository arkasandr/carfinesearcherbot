package ru.arkaleks.carfinesearcher.telegram.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static ru.arkaleks.carfinesearcher.telegram.constants.ButtonNameEnum.SENT_NUMBER_BUTTON;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();

        row1.add(new KeyboardButton(SENT_NUMBER_BUTTON.getButtonName()));
//        row1.add(new KeyboardButton(ButtonNameEnum.REDACT_DATA_BUTTON.getButtonName()));
//
//        KeyboardRow row2 = new KeyboardRow();
//        row2.add(new KeyboardButton(ButtonNameEnum.GENERATE_REPORT_BUTTON.getButtonName()));
//        row2.add(new KeyboardButton(ButtonNameEnum.HELP_BUTTON.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
//        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}