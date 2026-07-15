package com.abs.app.infrastructure.mapper;

import com.abs.app.application.user.appointment.dto.CreateAppointmentResponseDto;
import com.abs.app.domain.entity.Appointment;

public class AppointmentMapper {
    public static CreateAppointmentResponseDto toCreateAppointmentResponse(Appointment appointment) {
        CreateAppointmentResponseDto response = new CreateAppointmentResponseDto();
        if (appointment.getInvoice() != null) {
            response.setInvoiceId(appointment.getInvoice().getId());
        }
        return response;
    }
}
