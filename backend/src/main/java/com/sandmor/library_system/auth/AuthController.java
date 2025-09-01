package com.sandmor.library_system.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandmor.library_system.security.JwtService;
import com.sandmor.library_system.users.User;
import com.sandmor.library_system.users.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository userRepo, PasswordEncoder encoder, JwtService jwtService,
            AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
        var user = new User();
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole("ROLE_USER");
        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userRepo.findByEmail(request.email()).get();
        var token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
