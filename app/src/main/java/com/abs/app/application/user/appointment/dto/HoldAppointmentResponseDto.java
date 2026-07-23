package com.abs.app.application.user.appointment.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HoldAppointmentResponseDto {
    private String holdToken;
    private int expiresInSeconds;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
