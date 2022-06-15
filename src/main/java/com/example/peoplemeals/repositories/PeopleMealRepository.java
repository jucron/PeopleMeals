package com.example.peoplemeals.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@NoRepositoryBean
public interface PeopleMealRepository <E, I> extends JpaRepository<E, I> {

    Optional<E> findByUuid(UUID uuid);

    default E findRequiredById(I id) {
        return findById(id).orElseThrow(() ->
                new NoSuchElementException("No Element with this ID was found in Database"));
    }
    default E findRequiredByUuid(String uuid) {
        return findByUuid(UUID.fromString(uuid)).orElseThrow(() ->
                new NoSuchElementException("No Element with this UUID was found in Database"));
    }

//    @Query...
//    default I findIdRequiredById(I id) {
//        return (id).orElseThrow(() -> new NoSuchElementException(".."));
//    }
}
