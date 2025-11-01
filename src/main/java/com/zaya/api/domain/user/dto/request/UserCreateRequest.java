package com.zaya.api.domain.user.dto.request;
import com.zaya.api.domain.user.model.UserRole;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
  @NotBlank private String name;
  @Email @NotBlank private String email;
  @NotBlank @Size(min = 6) private String password;
  @NotBlank private String cellphone;
  @NotNull private UserRole role; // ADMIN ou USER
}