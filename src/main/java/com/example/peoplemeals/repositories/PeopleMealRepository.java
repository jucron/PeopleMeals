package com.example.peoplemeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@NoRepositoryBean
public interface PeopleMealRepository <E, I> extends JpaRepository<E, I> {

    Optional<E> findByUuid(UUID uuid);

    int countByUuid(UUID uuid);

    default E findRequiredByUuid(String uuid) {
        return findByUuid(UUID.fromString(uuid)).orElseThrow(() ->
                new NoSuchElementException("No Element with UUID: <"+uuid+"> was found in Database"));
    }
    default boolean isPresentRequiredByUuid(String uuid) {
        if (countByUuid(UUID.fromString(uuid))>0) {
            return true;
        } else {
            throw new NoSuchElementException("No Element with UUID: <"+uuid+"> was found in Database");
        }
    }


}
