package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private final Set<Long> likes = new HashSet<>();//Сет для АйДи лайкнувших
    private long id;
    @NotBlank(message = "Название фильма не может состоять из пробелов")
    @NotEmpty(message = "Название фильма не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Максимальная длина описания фильма — 200 символов")
    private final String description;
    @NotNull(message = "Дата выпуска фильма должна быть указана")
    private final LocalDate releaseDate;
    @PositiveOrZero(message = "Длительность фильма не может быть отрицательной")
    private final int duration;
}
