package com.example.configuration;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.security.RolesAllowed;

@Configuration
@EnableWebSecurity
// @RolesAllowed == @Secured -> which role allowed

// @RolesAllowed -> jsr250
// @Secured      -> Spring Framework
// @PreAuthorize -> prePostEnabled

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ApplicationSecurityConfiguration {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(@Autowired HttpSecurity http,
                                                   @Autowired SecurityFilter securityFilter) {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                // permitAll
                // authenticated
                // denyAll
                .antMatchers("/registration", "/login", "/public").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/admin/**/**").hasAuthority("ADMIN")

                // URL: admin**
                // administration
                // admins
                // |admin| ???

                // URL: admin/**
                // /admin/**/**

                .and()
                .logout().disable()

        ;

        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
