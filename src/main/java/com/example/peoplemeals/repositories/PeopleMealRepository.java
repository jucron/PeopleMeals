package com.example.peoplemeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@NoRepositoryBean
public interface PeopleMealRepository <E, I> extends JpaRepository<E, I> {

    Optional<E> findByUuid(UUID uuid);

    Optional<ProjectId> findIdByUuid(UUID uuid);

    default E findRequiredByUuid(String uuid) {
        return findByUuid(UUID.fromString(uuid)).orElseThrow(() ->
                new NoSuchElementException("No Element with UUID: <"+uuid+"> was found in Database"));
    }

    default long findIdRequiredByUuid(String uuid) {
        return findIdByUuid(UUID.fromString(uuid)).orElseThrow(() ->
                new NoSuchElementException("No Element with UUID: <"+uuid+"> was found in Database"))
                .getId();
    }
}
interface ProjectId {
    long getId();
}
