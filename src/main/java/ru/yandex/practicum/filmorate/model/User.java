package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotBlank(message = "Адрес почты не может состоять из пробелов")
    @NotEmpty(message = "Адрес почты не может быть пустым")
    @Email(message = "Неверный формат почты")
    private final String email;
    @NotBlank(message = "Логин не может состоять из пробелов")
    @NotEmpty(message = "Логин не может быть пустым")
    @Pattern(regexp = "[a-zA-Z0-9_\\-]+", message = "Логин может содержать только " +
            "буквы латинского алфавита, цифры, знак минус или нижнее подчёркивание")
    private final String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private final LocalDate birthday;
}
