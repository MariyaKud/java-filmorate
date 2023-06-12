package ru.yandex.practicum.filmorate.exeption;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException(String message) {
        super(message);
    }
}