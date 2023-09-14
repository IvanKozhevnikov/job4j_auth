package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.dto.PersonDto;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public boolean save(Person person) {
        if (findById(person.getId()).isEmpty()) {
            personRepository.save(person);
            return true;
        }
        return false;
    }

    public boolean update(PersonDto person) {
        Optional<Person> optionalDb = findById(person.getId());
        if (findById(person.getId()).isEmpty()) {
            return false;
        }
        optionalDb.get().setPassword(person.getPassword());
        personRepository.save(optionalDb.get());
        return true;
    }

    public Optional<Person> findByLogin(String id) {
        return personRepository.findByLogin(id);
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public boolean update(Person person) {
        if (findById(person.getId()).isPresent()) {
            personRepository.save(person);
            return true;
        }
        return false;
    }

    public boolean delete(Person id) {
        if (!personRepository.existsById(id.getId())) {
            throw new IllegalArgumentException("Person not found");
        }
        personRepository.delete(id);
        return true;
    }
}