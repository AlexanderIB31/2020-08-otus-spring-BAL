package ru.otus.spring.domain;

public class Answer {
    private final Integer value;

    public Answer(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "value=" + value +
                '}';
    }
}
