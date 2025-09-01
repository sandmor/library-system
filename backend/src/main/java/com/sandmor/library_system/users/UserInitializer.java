package com.sandmor.library_system.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserInitializer {
    @Bean
    CommandLineRunner initUsers(UserRepository userRepo, PasswordEncoder encoder) {
        return _ -> {
            if (userRepo.findByEmail("admin@example.com").isEmpty()) {
                var admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(encoder.encode("password"));
                admin.setRole("ROLE_ADMIN");
                userRepo.save(admin);
            }
            if (userRepo.findByEmail("user@example.com").isEmpty()) {
                var user = new User();
                user.setEmail("user@example.com");
                user.setPassword(encoder.encode("password"));
                user.setRole("ROLE_USER");
                userRepo.save(user);
            }
        };
    }
}
