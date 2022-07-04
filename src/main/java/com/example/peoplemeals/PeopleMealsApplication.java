package com.example.peoplemeals;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@NoCoverageGenerated
@SpringBootApplication
public class PeopleMealsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleMealsApplication.class, args);
    }
}
/*
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
                .withRole(Role.ADMIN)
        );
        credentialsService.createUser(new UserForm()
                .withUsername("user")
                .withPassword("user")
                .withRole(Role.USER));
    }
}

 */


