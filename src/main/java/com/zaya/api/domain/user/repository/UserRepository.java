package com.zaya.api.domain.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zaya.api.domain.user.model.User;

import org.springframework.data.domain.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String n, String e, Pageable p);

}
