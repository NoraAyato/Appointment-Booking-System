package com.abs.app.application.user.info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDto {
    private String userId;
    private String userName;
    private String email;
    private String picture;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String status;
    private String role;
    private boolean gender;
    private boolean isReceiveEmail;
}
