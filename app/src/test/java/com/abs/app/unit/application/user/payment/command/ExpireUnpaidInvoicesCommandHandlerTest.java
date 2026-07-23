package com.abs.app.unit.application.user.payment.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.abs.app.application.user.payment.command.ExpireUnpaidInvoicesCommandHandler;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class ExpireUnpaidInvoicesCommandHandlerTest {
    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ExpireUnpaidInvoicesCommandHandler handler;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(handler, "expirationMinutes", 15L);
        ReflectionTestUtils.setField(handler, "batchSize", 500);
    }

    @Test
    void shouldCancelExpiredUnpaidInvoicesAndRelatedAppointments() {
        List<String> invoiceIds = List.of("inv_001", "inv_002");
        when(invoiceRepository.findExpiredUnpaidInvoiceIds(eq(InvoiceStatus.UNPAID), any(LocalDateTime.class), eq(500)))
                .thenReturn(invoiceIds);
        when(invoiceRepository.cancelUnpaidInvoicesByIds(
                invoiceIds,
                InvoiceStatus.UNPAID,
                InvoiceStatus.CANCELLED))
                .thenReturn(2);

        int expiredInvoices = handler.handle();

        assertThat(expiredInvoices).isEqualTo(2);

        ArgumentCaptor<LocalDateTime> expiredBeforeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(invoiceRepository).findExpiredUnpaidInvoiceIds(
                eq(InvoiceStatus.UNPAID),
                expiredBeforeCaptor.capture(),
                eq(500));
        assertThat(expiredBeforeCaptor.getValue()).isBeforeOrEqualTo(LocalDateTime.now().minusMinutes(15).plusSeconds(1));

        verify(appointmentRepository).cancelPendingAppointmentsByInvoiceIds(
                invoiceIds,
                AppointmentStatus.PENDING,
                AppointmentStatus.CANCELLED);
        verify(paymentRepository).failPendingPaymentsByInvoiceIds(invoiceIds);
    }

    @Test
    void shouldSkipUpdatesWhenNoExpiredInvoiceExists() {
        when(invoiceRepository.findExpiredUnpaidInvoiceIds(eq(InvoiceStatus.UNPAID), any(LocalDateTime.class), eq(500)))
                .thenReturn(List.of());

        int expiredInvoices = handler.handle();

        assertThat(expiredInvoices).isZero();
        verify(invoiceRepository, never()).cancelUnpaidInvoicesByIds(any(), any(), any());
        verify(appointmentRepository, never()).cancelPendingAppointmentsByInvoiceIds(any(), any(), any());
        verify(paymentRepository, never()).failPendingPaymentsByInvoiceIds(any());
    }
}
