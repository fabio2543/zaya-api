package com.zaya.api.dto.user;
import com.zaya.api.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateRequest {
  @NotBlank private String nome;
  @Email @NotBlank private String email;
  private String senha;   // opcional: se vier, atualiza (re-hash)
  @NotNull private Role role;
}