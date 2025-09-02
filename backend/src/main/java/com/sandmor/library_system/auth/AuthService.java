package com.sandmor.library_system.auth;

import com.sandmor.library_system.security.JwtService;
import com.sandmor.library_system.users.User;
import com.sandmor.library_system.users.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @Transactional
    public User register(SignupRequest request) {
        var user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found in database. This should not happen."));

        var token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}