package com.zaya.api.dto.user;
import com.zaya.api.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
  @NotBlank private String nome;
  @Email @NotBlank private String email;
  @NotBlank @Size(min = 6) private String senha;
  @NotNull private Role role; // ADMIN ou USER
}