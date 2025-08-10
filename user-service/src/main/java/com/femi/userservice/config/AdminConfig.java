package com.femi.userservice.config;

import com.femi.userservice.model.Role;
import com.femi.userservice.model.User;
import com.femi.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class AdminConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> adminUsers = userRepository.findByRole(Role.ADMIN);
            if (adminUsers.isEmpty()) {
                User admin = User.builder()
                        .username("femmie")
                        .email("femifalase228@gmail.com")
                        .password(passwordEncoder.encode("Falasefemi228@"))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
