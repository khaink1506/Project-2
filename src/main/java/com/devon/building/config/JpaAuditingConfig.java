package com.devon.building.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return () -> {
            return Optional.ofNullable(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            );
        };
    }
}
