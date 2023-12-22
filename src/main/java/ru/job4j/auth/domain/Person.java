package ru.job4j.auth.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "person", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))

public class Person {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Id must be non null", groups = {
            Operation.OnDelete.class
    })
    private int id;

    @NotEmpty(message = "Login must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class
    })
    @Size(min = 4, message = "Login must be more than 4", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class,
    })
    private String login;

    @NotEmpty(message = "Password must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class,
    })
    @Size(min = 4, message = "Password must be more than 4", groups = {
            Operation.OnUpdate.class, Operation.OnCreate.class,
    })
    private String password;
}
