package com.abs.app.application.user.appointment.query;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.appointment.dto.BookingHistoryResponseDto;
import com.abs.app.common.response.PageResponse;
import com.abs.app.common.util.PaginationUtil;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.PaymentRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.service.ServiceBusinessHandle;
import com.abs.app.infrastructure.mapper.AppointmentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetBookingHistoryQueryHandler {
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final ServiceRepository serviceRepository;
    private final PaymentRepository paymentRepository;
    private final ServiceBusinessHandle serviceBusinessHandle;

    @Transactional(readOnly = true)
    public PageResponse<BookingHistoryResponseDto> handle(GetBookingHistoryQuery query) {
        Pageable pageable = PaginationUtil.createPageable(query.getPage(), query.getLimit());

        Page<AppointmentDetail> bookingHistory = appointmentDetailRepository.findBookingHistoryByCustomerId(
                query.getCustomerId(),
                pageable);

        List<String> serviceIds = bookingHistory.getContent().stream()
                .map(AppointmentDetail::getService)
                .filter(service -> service != null && service.getId() != null)
                .map(service -> service.getId())
                .distinct()
                .toList();
        List<ServiceImage> serviceImages = serviceRepository.findImagesByServiceIds(serviceIds);
        Map<String, List<String>> imagesByServiceId = serviceBusinessHandle.getImagesByServiceId(
                serviceIds,
                serviceImages);

        List<String> invoiceIds = bookingHistory.getContent().stream()
                .map(AppointmentDetail::getAppointment)
                .filter(appointment -> appointment != null && appointment.getInvoice() != null)
                .map(appointment -> appointment.getInvoice().getId())
                .filter(invoiceId -> invoiceId != null)
                .distinct()
                .toList();
        Map<String, Payment> latestPaymentsByInvoiceId = paymentRepository.findLatestByInvoiceIds(invoiceIds);

        return PaginationUtil.toPageResponse(
                bookingHistory,
                detail -> AppointmentMapper.toBookingHistoryResponse(
                        detail,
                        imagesByServiceId,
                        latestPaymentsByInvoiceId),
                query.getPage(),
                query.getLimit());
    }
}
