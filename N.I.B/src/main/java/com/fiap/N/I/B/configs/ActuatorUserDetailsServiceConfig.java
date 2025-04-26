package com.fiap.N.I.B.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ActuatorUserDetailsServiceConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        final var manager = new InMemoryUserDetailsManager();
        final var usuario= User.withUsername("admin").
                password(passwordEncoder.encode("admin"))
                .roles("ADMIN").build();
        manager.createUser(usuario);
        return manager;
    }




}
