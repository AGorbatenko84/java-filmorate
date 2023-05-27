package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {

    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0)
    private int duration;
    @NotNull
    private Set<Long> likes = new HashSet<>();

    private Integer rate;

    public void setLike(Long idUser) {
        likes.add(idUser);
    }

    public void deleteLike(Long idUser) {
        likes.remove(idUser);
    }
}
