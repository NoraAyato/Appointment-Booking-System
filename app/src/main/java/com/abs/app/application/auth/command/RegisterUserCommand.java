package com.abs.app.application.auth.command;

import com.abs.app.domain.entity.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserCommand {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
