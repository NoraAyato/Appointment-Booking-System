package com.abs.app.unit.application.user.appointment.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.abs.app.application.user.appointment.dto.BookingHistoryResponseDto;
import com.abs.app.application.user.appointment.query.GetBookingHistoryQuery;
import com.abs.app.application.user.appointment.query.GetBookingHistoryQueryHandler;
import com.abs.app.common.response.PageResponse;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Category;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.User;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.PaymentMethod;
import com.abs.app.domain.entity.enums.PaymentStatus;
import com.abs.app.domain.repository.AppointmentDetailRepository;
import com.abs.app.domain.repository.PaymentRepository;
import com.abs.app.domain.repository.ServiceRepository;
import com.abs.app.domain.service.ServiceBusinessHandle;

@ExtendWith(MockitoExtension.class)
class GetBookingHistoryQueryHandlerTest {
    private static final String CUSTOMER_ID = "u_customer_001";
    private static final String SERVICE_ID = "ser_001";
    private static final String INVOICE_ID = "inv_001";

    @Mock
    private AppointmentDetailRepository appointmentDetailRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ServiceBusinessHandle serviceBusinessHandle;

    @InjectMocks
    private GetBookingHistoryQueryHandler handler;

    private AppointmentDetail appointmentDetail;
    private ServiceImage serviceImage;
    private Payment payment;

    @BeforeEach
    void setup() {
        Category category = new Category();
        category.setName("Spa");
        category.setTagColor("#22C55E");

        ServiceEntity service = new ServiceEntity();
        service.setId(SERVICE_ID);
        service.setName("Massage body");
        service.setDurationMinutes(120);
        service.setCategory(category);

        User staff = new User();
        staff.setUserId("u_staff_001");
        staff.setFirstName("Nguyen");
        staff.setLastName("An");
        staff.setPicture("/images/staff/an.jpg");

        Promotion promotion = new Promotion();
        promotion.setCode("SPA20");

        Invoice invoice = new Invoice();
        invoice.setId(INVOICE_ID);
        invoice.setAmount(200000D);
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPromotion(promotion);

        Reviews reviews = new Reviews();
        reviews.setId("rev_001");

        Appointment appointment = new Appointment();
        appointment.setId("apt_001");
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setNote("Quiet room");
        appointment.setInvoice(invoice);
        appointment.setReviews(reviews);

        appointmentDetail = new AppointmentDetail();
        appointmentDetail.setId(1L);
        appointmentDetail.setStartTime(LocalDateTime.of(2026, 7, 15, 9, 0));
        appointmentDetail.setEndTime(LocalDateTime.of(2026, 7, 15, 11, 0));
        appointmentDetail.setQuantity(1);
        appointmentDetail.setService(service);
        appointmentDetail.setStaff(staff);
        appointmentDetail.setAppointment(appointment);

        serviceImage = new ServiceImage();
        serviceImage.setPicture("/images/services/massage.jpg");
        serviceImage.setService(service);

        payment = new Payment();
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentMethod(PaymentMethod.MOMO);
        payment.setInvoice(invoice);
    }

    @Test
    void shouldReturnPagedBookingHistoryWithBatchEnrichment() {
        when(appointmentDetailRepository.findBookingHistoryByCustomerId(eq(CUSTOMER_ID), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(appointmentDetail)));
        when(serviceRepository.findImagesByServiceIds(List.of(SERVICE_ID)))
                .thenReturn(List.of(serviceImage));
        when(serviceBusinessHandle.getImagesByServiceId(
                List.of(SERVICE_ID),
                List.of(serviceImage)))
                .thenReturn(Map.of(SERVICE_ID, List.of(serviceImage.getPicture())));
        when(paymentRepository.findLatestByInvoiceIds(List.of(INVOICE_ID)))
                .thenReturn(Map.of(INVOICE_ID, payment));

        PageResponse<BookingHistoryResponseDto> response = handler.handle(
                new GetBookingHistoryQuery(CUSTOMER_ID, 1, 10));

        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getLimit()).isEqualTo(10);
        assertThat(response.getTotal()).isEqualTo(1);
        assertThat(response.getItems()).singleElement().satisfies(item -> {
            assertThat(item.getAppointmentId()).isEqualTo("apt_001");
            assertThat(item.getServiceName()).isEqualTo("Massage body");
            assertThat(item.getServiceImage()).isEqualTo("/images/services/massage.jpg");
            assertThat(item.getStaffName()).isEqualTo("Nguyen An");
            assertThat(item.getInvoiceId()).isEqualTo(INVOICE_ID);
            assertThat(item.getPaymentStatus()).isEqualTo("PAID");
            assertThat(item.getPaymentMethod()).isEqualTo("MOMO");
            assertThat(item.isReviewed()).isTrue();
        });

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(appointmentDetailRepository).findBookingHistoryByCustomerId(
                eq(CUSTOMER_ID),
                pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
        verify(serviceRepository).findImagesByServiceIds(anyList());
        verify(paymentRepository).findLatestByInvoiceIds(anyList());
    }
}
