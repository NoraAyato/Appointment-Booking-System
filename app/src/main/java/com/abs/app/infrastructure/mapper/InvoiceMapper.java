package com.abs.app.infrastructure.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.abs.app.application.user.invoice.dto.InvoiceResponseDto;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.User;

public class InvoiceMapper {
    private InvoiceMapper() {
    }

    public static InvoiceResponseDto toInvoiceResponse(
            Invoice invoice,
            AppointmentDetail detail,
            String serviceImage,
            List<String> staffSpecializations,
            String discountValue) {
        InvoiceResponseDto dto = new InvoiceResponseDto();
        dto.setInvoiceId(invoice.getId());

        dto.setCreatedAt(invoice.getCreatedAt());
        dto.setInvoiceStatus(invoice.getStatus() != null ? invoice.getStatus().name() : null);
        dto.setServiceImage(serviceImage);
        dto.setStaffSpecializations(staffSpecializations != null ? staffSpecializations : List.of());

        if (invoice.getPromotion() != null) {
            dto.setPromotionCode(invoice.getPromotion().getCode());
            dto.setDiscountValue(discountValue);
        }
        mapAppointment(dto, invoice.getAppointment());
        mapAppointmentDetail(dto, detail);

        return dto;
    }

    private static void mapAppointment(InvoiceResponseDto dto, Appointment appointment) {
        if (appointment == null) {
            return;
        }

        dto.setAppointmentStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);
        dto.setNote(appointment.getNote());
    }

    private static void mapAppointmentDetail(InvoiceResponseDto dto, AppointmentDetail detail) {
        if (detail == null) {
            return;
        }

        LocalDateTime startTime = detail.getStartTime();
        LocalDateTime endTime = detail.getEndTime();
        if (startTime != null) {
            dto.setBookingDate(startTime.toLocalDate());
            dto.setStartTime(startTime.toLocalTime());
        }
        if (endTime != null) {
            dto.setEndTime(endTime.toLocalTime());
        }

        mapService(dto, detail.getService());
        mapStaff(dto, detail.getStaff());
    }

    private static void mapService(InvoiceResponseDto dto, ServiceEntity service) {
        if (service == null) {
            return;
        }
        dto.setTotalPrice(service.getPrice() != null ? String.valueOf(service.getPrice()) : "0");
        dto.setServiceName(service.getName());
        dto.setServiceDescription(service.getDescription());
        dto.setDuration(service.getDurationMinutes());

        Category category = service.getCategory();
        if (category != null) {
            dto.setCategoryName(category.getName());
            dto.setCategoryColorTag(category.getTagColor());
        }
    }

    private static void mapStaff(InvoiceResponseDto dto, User staff) {
        if (staff == null) {
            return;
        }

        String firstName = staff.getFirstName() != null ? staff.getFirstName() : "";
        String lastName = staff.getLastName() != null ? staff.getLastName() : "";
        dto.setStaffName((firstName + " " + lastName).trim());
        dto.setStaffImage(staff.getPicture());
    }
}
