package com.zaya.api.domain.auth.controller;

import java.time.LocalDateTime;

import com.zaya.api.domain.auth.dto.request.LoginRequest;
import com.zaya.api.domain.auth.dto.request.RegisterRequest;
import com.zaya.api.domain.auth.dto.response.AuthResponse;
import com.zaya.api.domain.auth.service.JwtService;
import com.zaya.api.domain.user.model.User;
import com.zaya.api.domain.user.model.UserStatus;
import com.zaya.api.domain.user.repository.UserRepository;

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
                .name(req.getName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .status(UserStatus.ATIVO)
                .cellphone(req.getCellphone())
                .dateCreate(LocalDateTime.now())
                .dateUpdate(null)
                .dateLastLogin(null)  
                .build();

        userRepository.save(user);
        return ResponseEntity.status(200).body("Usuário registrado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !encoder.matches(req.getSenha(), user.getPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
        user.setDateLastLogin(LocalDateTime.now());
        userRepository.save(user);
        var token = jwt.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}