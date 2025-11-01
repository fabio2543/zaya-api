package com.zaya.api.domain.user.dto.response;
import java.time.LocalDateTime;

import com.zaya.api.domain.user.model.UserRole;
import com.zaya.api.domain.user.model.UserStatus;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private UserRole role;
  private String cellphone;
  private UserStatus status;
  private LocalDateTime dateCreate;
  private LocalDateTime dateUpdate;
  private LocalDateTime dateLastLogin;

}