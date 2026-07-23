package com.abs.app.application.admin.usermanager.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String joinDate;
    private String avatar;
}
