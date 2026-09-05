package com.devon.building.config;


import com.devon.building.constant.SystemConstant;
import com.devon.building.filters.JwtTokenFilter;
import com.devon.building.security.CustomSuccessHandler;
import com.devon.building.service.impl.CustomOidUserService;
import com.devon.building.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOidUserService customOidUserService) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/buildings/**").hasAnyRole(SystemConstant.USER, SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.POST, "/api/buildings").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/buildings/assign").hasRole(SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.PUT, "/api/buildings").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .requestMatchers(HttpMethod.DELETE, "/api/buildings/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers("/admin/users/**").hasRole(SystemConstant.MANAGER)
                        .requestMatchers("/admin/**").hasAnyRole(SystemConstant.STAFF, SystemConstant.MANAGER)
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/j_spring_security_check")
                        .successHandler(myAuthenticationSuccessHandler())
                        .failureUrl("/login?incorrectAccount")
                        .usernameParameter("userName")
                        .passwordParameter("password")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidUserService))
                        .successHandler(myAuthenticationSuccessHandler())
                        .failureHandler((request, response, exception) -> {
                           log.error("Oauth2 login fail", exception);
                            response.sendRedirect("/login?incorrectAccount");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomSuccessHandler();
    }
}
