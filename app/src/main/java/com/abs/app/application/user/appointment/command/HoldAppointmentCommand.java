package com.abs.app.application.user.appointment.command;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HoldAppointmentCommand {
    private String customerId;
    private String serviceId;
    private String staffId;
    private LocalDate date;
    private LocalTime time;
}
