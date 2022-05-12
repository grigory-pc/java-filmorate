package ru.yandex.practicum.filmorate.exception;

public class InvalidLoginException extends Exception{

    public InvalidLoginException(String message) {
        super(message);
    }
}
