package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class Genre {

    private int id;
    @NotBlank
    private String name;
}
