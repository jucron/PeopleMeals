package com.example.peoplemeals.repositories;

import com.example.peoplemeals.domain.security.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findByUsername(String username);

    default Credentials findRequiredByUsername(String username) {
        return findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found in database"));
    }

}
