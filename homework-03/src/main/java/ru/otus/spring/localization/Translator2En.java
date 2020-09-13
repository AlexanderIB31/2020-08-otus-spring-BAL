package ru.otus.spring.localization;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("translator2En")
public class Translator2En implements Translator {
    private final MessageSource messageSource;

    public Translator2En(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String s) {
        return messageSource.getMessage(s, null, Locale.ENGLISH);
    }
}
