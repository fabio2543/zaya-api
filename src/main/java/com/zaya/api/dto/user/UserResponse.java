package com.zaya.api.dto.user;
import com.zaya.api.model.Role;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class UserResponse {
  private Long id;
  private String nome;
  private String email;
  private Role role;
}