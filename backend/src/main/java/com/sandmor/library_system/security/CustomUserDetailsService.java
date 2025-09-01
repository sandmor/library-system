package com.sandmor.library_system.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.sandmor.library_system.users.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userReṕo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userReṕo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userReṕo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}
