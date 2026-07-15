package com.abs.app.application.user.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.abs.app.common.constant.AppointmentConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HoldAppointmentRequestDto {
    @NotBlank(message = AppointmentConstant.VALID_SERVICE_ID)
    private String serviceId;

    @NotBlank(message = AppointmentConstant.VALID_STAFF_ID)
    private String staffId;

    @NotNull(message = AppointmentConstant.VALID_DATE)
    private LocalDate date;

    @NotNull(message = AppointmentConstant.VALID_TIME)
    private LocalTime time;
}
