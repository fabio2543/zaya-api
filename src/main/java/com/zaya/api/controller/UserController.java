package com.zaya.api.controller;

import com.zaya.api.dto.user.*;
import com.zaya.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // toda a classe restrita a ADMIN
public class UserController {

  private final UserService userService;

  @GetMapping
  public Page<UserResponse> list(
      @RequestParam(required = false) String q,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return userService.list(q, page, size);
  }

  @GetMapping("/{id}")
  public UserResponse get(@PathVariable Long id) {
    return userService.get(id);
  }

  @PostMapping
  public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
    return userService.create(req);
  }

  @PutMapping("/{id}")
  public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
    return userService.update(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    userService.delete(id);
  }
}
