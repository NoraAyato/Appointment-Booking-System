package com.abs.app.application.user.appointment.dto;

import com.abs.app.common.constant.AppointmentConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAppointmentRequestDto {
    @NotBlank(message = AppointmentConstant.VALID_HOLD_TOKEN)
    private String holdToken;

    @Size(max = AppointmentConstant.NOTE_MAX_LENGTH, message = AppointmentConstant.VALID_NOTE_SIZE)
    private String note;
}
