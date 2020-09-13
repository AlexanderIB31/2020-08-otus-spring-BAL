package ru.otus.spring.localization;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("translator2Ru")
public class Translator2Ru implements Translator {
    private final MessageSource messageSource;

    public Translator2Ru(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String s) {
        return messageSource.getMessage(s, null, new Locale("ru", "RU"));
    }
}
