package ru.job4j.auth.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private int id;
    private String password;
}