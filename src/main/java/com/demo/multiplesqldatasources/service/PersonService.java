package com.demo.multiplesqldatasources.service;

import com.demo.multiplesqldatasources.model.entity.Person;
import com.demo.multiplesqldatasources.repository.primary.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        personRepository.findAll().forEach(people::add);
        return people;
    }
}
