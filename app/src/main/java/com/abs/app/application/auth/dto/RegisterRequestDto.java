package com.abs.app.application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank
    @Size(min = 5, max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 10)
    private String password;

    @NotBlank
    @Size(min = 3, max = 6)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 6)
    private String lastName;
}
