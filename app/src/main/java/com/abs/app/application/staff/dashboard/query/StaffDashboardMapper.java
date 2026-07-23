package com.abs.app.application.staff.dashboard.query;

import java.time.LocalDate;

import com.abs.app.application.staff.dashboard.dto.StaffScheduleEventResponseDto;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.BlockedSlot;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.StaffShift;
import com.abs.app.domain.entity.User;

public class StaffDashboardMapper {
    private static final String SHIFT_TYPE = "SHIFT";
    private static final String APPOINTMENT_TYPE = "APPOINTMENT";
    private static final String BLOCKED_SLOT_TYPE = "BLOCKED_SLOT";

    private StaffDashboardMapper() {
    }

    public static StaffScheduleEventResponseDto toAppointmentEvent(AppointmentDetail appointmentDetail) {
        StaffScheduleEventResponseDto dto = new StaffScheduleEventResponseDto();
        dto.setType(APPOINTMENT_TYPE);
        dto.setAppointmentDetailId(appointmentDetail.getId());
        dto.setDate(appointmentDetail.getStartTime() != null ? appointmentDetail.getStartTime().toLocalDate() : null);
        dto.setStartTime(appointmentDetail.getStartTime() != null ? appointmentDetail.getStartTime().toLocalTime() : null);
        dto.setEndTime(appointmentDetail.getEndTime() != null ? appointmentDetail.getEndTime().toLocalTime() : null);

        Appointment appointment = appointmentDetail.getAppointment();
        if (appointment != null) {
            dto.setAppointmentId(appointment.getId());
            dto.setStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);

            User customer = appointment.getCustomer();
            if (customer != null) {
                dto.setCustomerName(fullName(customer));
                dto.setCustomerPhone(customer.getPhoneNumber());
            }
        }

        ServiceEntity service = appointmentDetail.getService();
        if (service != null) {
            dto.setTitle(service.getName());
            dto.setServiceId(service.getId());
            dto.setServiceName(service.getName());
        }
        return dto;
    }

    public static StaffScheduleEventResponseDto toShiftEvent(StaffShift shift) {
        StaffScheduleEventResponseDto dto = new StaffScheduleEventResponseDto();
        dto.setType(SHIFT_TYPE);
        dto.setShiftId(shift.getId());
        dto.setDate(shift.getWorkDate());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        dto.setStatus(shift.getStatus() != null ? shift.getStatus().name() : null);
        dto.setTitle("Ca làm việc");
        return dto;
    }

    public static StaffScheduleEventResponseDto toBlockedSlotEvent(BlockedSlot blockedSlot, LocalDate eventDate) {
        StaffScheduleEventResponseDto dto = new StaffScheduleEventResponseDto();
        dto.setType(BLOCKED_SLOT_TYPE);
        dto.setBlockedSlotId(blockedSlot.getId());
        dto.setDate(eventDate);
        dto.setStartTime(blockedSlot.getStartTime());
        dto.setEndTime(blockedSlot.getEndTime());
        dto.setStatus(blockedSlot.getStatus() != null ? blockedSlot.getStatus().name() : null);
        dto.setTitle("Thời gian bận");
        dto.setReason(blockedSlot.getReason());
        return dto;
    }

    private static String fullName(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
