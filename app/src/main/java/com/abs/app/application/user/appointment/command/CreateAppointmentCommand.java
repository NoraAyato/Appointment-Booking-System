package com.abs.app.application.user.appointment.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAppointmentCommand {
    private String customerId;
    private String holdToken;
    private String note;
}
