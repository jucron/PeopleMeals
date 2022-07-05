package com.example.peoplemeals;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.domain.security.Role;
import com.example.peoplemeals.helpers.NoCoverageGenerated;
import com.example.peoplemeals.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@NoCoverageGenerated
@SpringBootApplication
public class PeopleMealsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleMealsApplication.class, args);
    }
}

@Profile("default")
@NoCoverageGenerated
@Component
@RequiredArgsConstructor
@Slf4j
class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {
    private final CredentialsService credentialsService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Creating Admin and User");
        credentialsService.createUser(new UserForm()
                .withUsername("admin")
                .withPassword("admin")
                .withRole(Role.ADMIN.role.toUpperCase())
        );
        credentialsService.createUser(new UserForm()
                .withUsername("user")
                .withPassword("user")
                .withRole(Role.USER.role.toUpperCase()));
    }
}




