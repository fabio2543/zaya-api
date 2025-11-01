package com.zaya.api.domain.auth.dto.request;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String senha;
    private LocalDateTime dataUltimoLogin;
}
