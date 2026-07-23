package com.abs.app.application.staff.appointment.query;

import com.abs.app.application.staff.appointment.dto.StaffAppointmentResponseDto;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.User;

public class StaffAppointmentMapper {
    private StaffAppointmentMapper() {
    }

    public static StaffAppointmentResponseDto toResponse(AppointmentDetail appointmentDetail) {
        StaffAppointmentResponseDto dto = new StaffAppointmentResponseDto();
        dto.setAppointmentDetailId(appointmentDetail.getId());
        dto.setStartTime(appointmentDetail.getStartTime());
        dto.setEndTime(appointmentDetail.getEndTime());
        dto.setQuantity(appointmentDetail.getQuantity());
        dto.setPicture(appointmentDetail.getPicture());

        Appointment appointment = appointmentDetail.getAppointment();
        if (appointment != null) {
            dto.setAppointmentId(appointment.getId());
            dto.setStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);
            dto.setNote(appointment.getNote());

            User customer = appointment.getCustomer();
            if (customer != null) {
                dto.setCustomerName(fullName(customer));
                dto.setCustomerPhone(customer.getPhoneNumber());
                dto.setCustomerAvatar(customer.getPicture());
            }
        }

        ServiceEntity service = appointmentDetail.getService();
        if (service != null) {
            dto.setServiceId(service.getId());
            dto.setServiceName(service.getName());
        }

        return dto;
    }

    private static String fullName(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
