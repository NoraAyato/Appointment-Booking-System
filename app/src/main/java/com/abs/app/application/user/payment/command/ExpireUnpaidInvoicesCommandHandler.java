package com.abs.app.application.user.payment.command;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.repository.AppointmentRepository;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpireUnpaidInvoicesCommandHandler {
    private final InvoiceRepository invoiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;

    @Value("${payment.expiration.unpaid-invoice-minutes:15}")
    private long expirationMinutes;

    @Value("${payment.expiration.batch-size:500}")
    private int batchSize;

    @Transactional
    public int handle() {
        LocalDateTime expiredBefore = LocalDateTime.now().minusMinutes(expirationMinutes);
        List<String> invoiceIds = invoiceRepository.findExpiredUnpaidInvoiceIds(
                InvoiceStatus.UNPAID,
                expiredBefore,
                batchSize);

        if (invoiceIds.isEmpty()) {
            return 0;
        }

        int cancelledInvoices = invoiceRepository.cancelUnpaidInvoicesByIds(
                invoiceIds,
                InvoiceStatus.UNPAID,
                InvoiceStatus.CANCELLED);

        appointmentRepository.cancelPendingAppointmentsByInvoiceIds(
                invoiceIds,
                AppointmentStatus.PENDING,
                AppointmentStatus.CANCELLED);
        paymentRepository.failPendingPaymentsByInvoiceIds(invoiceIds);

        return cancelledInvoices;
    }
}
