package com.zaya.api.controller;

import com.zaya.api.dto.AuthResponse;
import com.zaya.api.dto.LoginRequest;
import com.zaya.api.dto.RegisterRequest;
import com.zaya.api.model.Role;
import com.zaya.api.model.User;
import com.zaya.api.repository.UserRepository;
import com.zaya.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }
        var user = User.builder()
                .nome(req.getNome())
                .email(req.getEmail())
                .senha(encoder.encode(req.getSenha()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var token = jwt.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !encoder.matches(req.getSenha(), user.getSenha())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
        var token = jwt.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}