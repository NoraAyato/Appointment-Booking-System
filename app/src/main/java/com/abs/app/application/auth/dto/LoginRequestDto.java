package com.abs.app.application.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private boolean rememberMe;

}
