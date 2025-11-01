// src/main/java/com/zaya/api/service/UserService.java
package com.zaya.api.domain.user.service;

import com.zaya.api.domain.user.dto.request.UserCreateRequest;
import com.zaya.api.domain.user.dto.request.UserUpdateRequest;
import com.zaya.api.domain.user.dto.response.UserResponse;
import com.zaya.api.domain.user.model.User;
import com.zaya.api.domain.user.model.UserRole;
import com.zaya.api.domain.user.model.UserStatus;
import com.zaya.api.domain.user.repository.UserRepository;
import com.zaya.api.domain.user.service.policy.AdminFirstLoginPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private AdminFirstLoginPolicy adminFirstLoginPolicy;

    public Page<UserResponse> list(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> result;

        if (q == null || q.isBlank()) {
            result = userRepository.findAll(pageable);
        } else {
            result = userRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable);
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
                .name(req.getName())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .status(UserStatus.ATIVO)
                .cellphone(req.getCellphone())
                .dateCreate(LocalDateTime.now())
                .dateUpdate(LocalDateTime.now())
                .dateLastLogin(null)  
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UserUpdateRequest req) {
        var user = userRepository.findById(id).orElseThrow(() ->new NoSuchElementException("Usuário não encontrado"));
        if (adminFirstLoginPolicy.canApplyFullUpdate(user)) {
            canApplyFullUpdate(user, req);
        }
        else {
            applyPatch(user, req);
        }
        user.setDateUpdate(req.getDateUpdate());
   
        return toResponse(userRepository.save(user));
    }
    
    // Aplica atualização completa para Admin no primeiro login
    public void canApplyFullUpdate(User user, UserUpdateRequest req) {

        if (StringUtils.hasText(req.getName())) {
            user.setName(req.getName()); 
        }
        else {
            throw new IllegalArgumentException("Nome é obrigatório para atualização completa no primeiro login");
        }
        if (StringUtils.hasText(req.getEmail())) {
            user.setEmail(req.getEmail());
        }
        else {
            throw new IllegalArgumentException("E-mail é obrigatório para atualização completa no primeiro login");
        }
        if (StringUtils.hasText(req.getPassword())) {
            user.setPassword(encoder.encode(req.getPassword()));
        } else {
            throw new IllegalArgumentException("Senha é obrigatória para atualização completa no primeiro login");
        }
        if (StringUtils.hasText(req.getCellphone())) {
            user.setCellphone(req.getCellphone()); 
        } else {
            throw new IllegalArgumentException("Celular é obrigatório para atualização completa no primeiro login");
        }
        user.setStatus(UserStatus.ATIVO);
        user.setRole(UserRole.ADMIN);
    }
    // Aplica atualização parcial para outros casos
    public void applyPatch(User user, UserUpdateRequest req){

        if (StringUtils.hasText(req.getPassword())) {
            user.setPassword(encoder.encode(req.getPassword()));
        }
        if (req.getStatus() != null) {
            user.setStatus(req.getStatus());
        }
        if (StringUtils.hasText(req.getCellphone())) {
            user.setCellphone(req.getCellphone());
        }
        if (req.getRole() != null) {
            user.setRole(req.getRole());
        }
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
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole())
                .cellphone(u.getCellphone())
                .status(u.getStatus())
                .dateCreate(u.getDateCreate())
                .dateUpdate(u.getDateUpdate())
                .dateLastLogin(u.getDateLastLogin())
                .build();
    }
}
