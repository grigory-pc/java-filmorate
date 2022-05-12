package ru.yandex.practicum.filmorate.exception;

public class NegativeDurationException extends Exception{

    public NegativeDurationException(String message) {
        super(message);
    }
}
