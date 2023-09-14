package ru.job4j.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
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
        try {
            return personRepository.save(person).getId() != 0;
        } catch (Exception e) {
            log.error("Exception person dont save", e);
        }
        return false;
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
