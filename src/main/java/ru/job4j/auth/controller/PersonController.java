package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDto;
import ru.job4j.auth.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@ControllerAdvice
@AllArgsConstructor
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    @GetMapping("/all")
    public List<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.personService.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        var passwordIn = person.getPassword();
        var loginIn = person.getLogin();
        var loginDb = personService.findByLogin(person.getLogin());
        if (loginDb.isPresent()) {
            throw new IllegalArgumentException("Such id already exists.");
        }
        if (loginIn.length() < 3 || passwordIn.length() < 3) {
            throw new IllegalArgumentException("Invalid login or password. Length must be more than 3 characters.");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                person,
                this.personService.save(person) ? HttpStatus.CREATED : HttpStatus.CONFLICT
        );
    }

    @PutMapping("/")
    public ResponseEntity<Person> update(@RequestBody Person person) {
        var passwordIn = person.getPassword();
        var loginIn = person.getLogin();
        var loginDb = personService.findByLogin(person.getLogin());
        if (loginDb.isPresent()) {
            throw new IllegalArgumentException("Such id already exists.");
        }
        if (loginIn.length() < 4 || passwordIn.length() < 4) {
            throw new IllegalArgumentException("Invalid login or password. Length must be more than 3 characters.");
        }
        return new ResponseEntity<Person>(
                this.personService.update(person) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        return new ResponseEntity<>(
                this.personService.delete(person) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", "Some of fields empty");
                put("details", e.getMessage());
            }
        }));
        LOGGER.error(e.getMessage());
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(@RequestBody PersonDto personDto) {
        String newPassword = personDto.getPassword();
        if (newPassword == null) {
            throw new NullPointerException("Password mustn't be empty");
        }
        if (newPassword.length() < 4) {
            throw new IllegalArgumentException("Invalid password. Password length must be more than 4 characters.");
        }
        var optionalDb = personService.findById(personDto.getId());
        if (optionalDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        personDto.setPassword(encoder.encode(personDto.getPassword()));
        return personService.update(personDto) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}