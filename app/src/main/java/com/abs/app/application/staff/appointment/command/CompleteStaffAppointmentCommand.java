package com.abs.app.application.staff.appointment.command;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompleteStaffAppointmentCommand {
    private String staffId;
    private String appointmentId;
    private MultipartFile picture;
}
