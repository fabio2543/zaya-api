// src/main/java/com/zaya/api/service/UserService.java
package com.zaya.api.service;

import com.zaya.api.dto.user.*;
import com.zaya.api.model.User;
import com.zaya.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Page<UserResponse> list(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> result;

        if (q == null || q.isBlank()) {
            result = userRepository.findAll(pageable);
        } else {
            result = userRepository
                    .findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable);
        }
        return result.map(this::toResponse);
    }

    public UserResponse get(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        return toResponse(user);
    }

    public UserResponse create(UserCreateRequest req) {
        userRepository.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("E-mail já cadastrado");
        });
        var user = User.builder()
                .nome(req.getNome())
                .email(req.getEmail())
                .senha(encoder.encode(req.getSenha()))
                .role(req.getRole())
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserUpdateRequest req) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (!user.getEmail().equalsIgnoreCase(req.getEmail())) {
            userRepository.findByEmail(req.getEmail()).ifPresent(u -> {
                throw new IllegalArgumentException("E-mail já cadastrado");
            });
            user.setEmail(req.getEmail());
        }
        user.setNome(req.getNome());
        user.setRole(req.getRole());

        if (req.getSenha() != null && !req.getSenha().isBlank()) {
            user.setSenha(encoder.encode(req.getSenha()));
        }

        return toResponse(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .role(u.getRole())
                .build();
    }
}
