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

    public Person save(Person person) {
            return personRepository.save(person);
        }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public void delete(Person id) {
        if (!personRepository.existsById(id.getId())) {
            throw new IllegalArgumentException("Accident not found");
        }
        personRepository.delete(id);

    }
}
