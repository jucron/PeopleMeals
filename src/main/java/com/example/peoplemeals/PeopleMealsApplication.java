package com.example.peoplemeals;

import com.example.peoplemeals.api.v1.model.forms.UserForm;
import com.example.peoplemeals.domain.security.Role;
import com.example.peoplemeals.services.CredentialsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PeopleMealsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleMealsApplication.class, args);
    }
}

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
                .withRole(Role.ADMIN)
        );
        credentialsService.createUser(new UserForm()
                .withUsername("user")
                .withPassword("user")
                .withRole(Role.USER));
    }
}
