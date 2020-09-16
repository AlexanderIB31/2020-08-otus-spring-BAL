package ru.otus.spring.localization;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.spring.Props;

@Component
public class PropsTranslator implements Translator {
    private final MessageSource messageSource;
    private final Props props;

    public PropsTranslator(MessageSource messageSource, Props props) {
        this.messageSource = messageSource;
        this.props = props;
    }

    @Override
    public String getMessage(String text) {
        return messageSource.getMessage(text, null, props.getLocale());
    }
}
