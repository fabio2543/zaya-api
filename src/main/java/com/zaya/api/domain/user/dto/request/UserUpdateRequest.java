package com.zaya.api.domain.user.dto.request;
import java.time.LocalDateTime;

import com.zaya.api.domain.user.model.UserRole;
import com.zaya.api.domain.user.model.UserStatus;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateRequest {
  private String name;
  @Email
  private String email;
  private String password;   // opcional: se vier, atualiza (re-hash)
  private UserRole role;
  private UserStatus status;
  private LocalDateTime dateUpdate;
  private String cellphone;
  
}