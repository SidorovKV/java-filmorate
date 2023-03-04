package ru.yandex.practicum.filmorate.exceptions;

public class UnknownEntityException  extends RuntimeException {
    public UnknownEntityException(String s) {
        super(s);
    }
}