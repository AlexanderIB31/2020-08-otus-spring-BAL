package ru.otus.spring;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "question")
public class Props {
    private String uri;
    private long threshold;
    private Locale locale;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getThreshold() {
        return this.threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = LocaleUtils.toLocale(locale);
    }
}
