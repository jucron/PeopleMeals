package com.example.peoplemeals.config;

import com.example.peoplemeals.helpers.NoCoverageGenerated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@NoCoverageGenerated
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class PersistenceConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new CustomAuditorAware();
    }
}
