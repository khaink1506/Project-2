package com.devon.building.config;

import jakarta.servlet.http.HttpServletRequest;
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
    public AuditorAware<String> auditorProvider(HttpServletRequest request){
        return () -> {

            // REGISTER
            if ("/api/user/register".equals(request.getRequestURI())) {
                return Optional.empty();
            }
            return Optional.ofNullable(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            );
        };
    }
}
