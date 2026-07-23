package com.abs.app.infrastructure.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.abs.app.application.user.appointment.dto.BookingHistoryResponseDto;
import com.abs.app.application.user.appointment.dto.CreateAppointmentResponseDto;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.User;

public class AppointmentMapper {
    private AppointmentMapper() {
    }

    public static CreateAppointmentResponseDto toCreateAppointmentResponse(Appointment appointment) {
        CreateAppointmentResponseDto response = new CreateAppointmentResponseDto();
        if (appointment.getInvoice() != null) {
            response.setInvoiceId(appointment.getInvoice().getId());
        }
        return response;
    }

    public static BookingHistoryResponseDto toBookingHistoryResponse(
            AppointmentDetail detail,
            Map<String, List<String>> imagesByServiceId,
            Map<String, Payment> latestPaymentsByInvoiceId) {
        BookingHistoryResponseDto dto = new BookingHistoryResponseDto();
        dto.setAppointmentDetailId(detail.getId());
        dto.setQuantity(detail.getQuantity());

        mapBookingTime(dto, detail);
        mapService(dto, detail.getService(), imagesByServiceId);
        mapStaff(dto, detail.getStaff());
        mapAppointment(dto, detail.getAppointment(), latestPaymentsByInvoiceId);
        return dto;
    }

    private static void mapBookingTime(BookingHistoryResponseDto dto, AppointmentDetail detail) {
        LocalDateTime startTime = detail.getStartTime();
        if (startTime != null) {
            dto.setBookingDate(startTime.toLocalDate());
            dto.setStartTime(startTime.toLocalTime());
        }
        if (detail.getEndTime() != null) {
            dto.setEndTime(detail.getEndTime().toLocalTime());
        }
    }

    private static void mapService(
            BookingHistoryResponseDto dto,
            ServiceEntity service,
            Map<String, List<String>> imagesByServiceId) {
        if (service == null) {
            return;
        }

        dto.setServiceId(service.getId());
        dto.setServiceName(service.getName());
        dto.setDurationMinutes(service.getDurationMinutes());
        dto.setServiceImage(imagesByServiceId.getOrDefault(service.getId(), List.of())
                .stream()
                .findFirst()
                .orElse(null));

        Category category = service.getCategory();
        if (category != null) {
            dto.setCategoryName(category.getName());
            dto.setCategoryColorTag(category.getTagColor());
        }
    }

    private static void mapStaff(BookingHistoryResponseDto dto, User staff) {
        if (staff == null) {
            return;
        }

        dto.setStaffId(staff.getUserId());
        dto.setStaffName(fullName(staff));
        dto.setStaffAvatar(staff.getPicture());
    }

    private static void mapAppointment(
            BookingHistoryResponseDto dto,
            Appointment appointment,
            Map<String, Payment> latestPaymentsByInvoiceId) {
        if (appointment == null) {
            return;
        }

        dto.setAppointmentId(appointment.getId());
        dto.setAppointmentStatus(appointment.getStatus() != null ? appointment.getStatus().name() : null);
        dto.setNote(appointment.getNote());
        dto.setReviewed(appointment.getReviews() != null);

        mapInvoice(dto, appointment.getInvoice(), latestPaymentsByInvoiceId);
    }

    private static void mapInvoice(
            BookingHistoryResponseDto dto,
            Invoice invoice,
            Map<String, Payment> latestPaymentsByInvoiceId) {
        if (invoice == null) {
            return;
        }

        dto.setInvoiceId(invoice.getId());
        dto.setInvoiceAmount(invoice.getAmount());
        dto.setInvoiceStatus(invoice.getStatus() != null ? invoice.getStatus().name() : null);
        if (invoice.getPromotion() != null) {
            dto.setPromotionCode(invoice.getPromotion().getCode());
        }

        Payment payment = latestPaymentsByInvoiceId.get(invoice.getId());
        if (payment != null) {
            dto.setPaymentStatus(payment.getStatus() != null ? payment.getStatus().name() : null);
            dto.setPaymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null);
        }
    }

    private static String fullName(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }
}
