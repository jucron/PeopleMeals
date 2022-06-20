package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends PeopleMealRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE p.id NOT IN :listOfPersons")
    List<Person> findAllNotInList(List<Long> listOfPersons);
}
