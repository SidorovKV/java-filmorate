package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotBlank(message = "Название не может состоять из пробелов")
    @NotEmpty(message = "Название не может быть пустым")
    private final String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private final String description;
    @NotNull(message = "Дата выпуска должна быть указана")
    private final LocalDate releaseDate;
    @PositiveOrZero(message = "Длительность не может быть отрицательной")
    private final int duration;
}
