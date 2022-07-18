package com.demo.multiplesqldatasources.repository.primary;

import com.demo.multiplesqldatasources.model.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
}
