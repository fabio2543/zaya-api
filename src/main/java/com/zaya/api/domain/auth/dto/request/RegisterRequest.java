package com.zaya.api.domain.auth.dto.request;

import com.zaya.api.domain.user.model.UserRole;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private String cellphone;
}
