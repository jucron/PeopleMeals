package com.example.peoplemeals.services;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.api.v1.model.lists.EntityDTOList;
import com.example.peoplemeals.domain.security.Credentials;
import com.example.peoplemeals.repositories.CredentialsRepository;
import com.example.peoplemeals.repositories.PersonRepository;
import com.example.peoplemeals.services.validations.CredentialsValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CredentialsServiceImpl implements CredentialsService, UserDetailsService {

    private final CredentialsRepository credentialsRepository;
    private final CredentialsValidation credentialsValidation;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserForm form) {
        credentialsValidation.validateNoSameUsernameInDatabase(form.getUsername());
        credentialsValidation.validateRoleFormat(form.getRole());
        credentialsRepository.save(new Credentials()
                .withUsername(form.getUsername())
                .withPassword(passwordEncoder.encode(form.getPassword()))
                .withRole(form.getRole())
                //If personUuid is null, save a User without association to any Person
                .withPerson(form.getPersonUuid() == null ?
                        null : personRepository.findRequiredByUuid(form.getPersonUuid())));
    }

    @Override
    public void deactivateUser(String username) {
        Credentials userInRepo = credentialsRepository.findRequiredByUsername(username);
        userInRepo.setDeactivationDate(LocalDateTime.now());
        credentialsRepository.save(userInRepo);
    }

    @Override
    public EntityDTOList<Credentials> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        return new EntityDTOList<>(credentialsRepository
                .findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))).getContent()
                .stream()
                .peek(credentials -> credentials.setPassword("********"))
                .collect(Collectors.toList()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials userInRepo = credentialsRepository.findRequiredByUsername(username);
        String roleOfThisUser = credentialsValidation.validateCredentialsActive(userInRepo);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(List.of(
                new SimpleGrantedAuthority(roleOfThisUser)));

        return new User(userInRepo.getUsername(), userInRepo.getPassword(), authorities);
    }
}
