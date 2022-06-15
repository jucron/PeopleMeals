package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PeopleMealRepository<Person, Long> {
}
