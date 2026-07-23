package com.abs.app.application.user.payment.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.payment.dto.MomoIpnRequestDto;
import com.abs.app.common.constant.PaymentConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.config.MomoPaymentProperties;
import com.abs.app.domain.entity.Appointment;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.enums.AppointmentStatus;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.PaymentStatus;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.PaymentRepository;
import com.abs.app.infrastructure.payment.momo.MomoSignatureService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HandleMomoIpnCommandHandler {
    private static final int MOMO_SUCCESS_CODE = 0;

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final MomoPaymentProperties momoPaymentProperties;
    private final MomoSignatureService momoSignatureService;

    @Transactional
    public void handle(MomoIpnRequestDto request) {
        if (!momoSignatureService.verify(buildRawSignature(request), request.getSignature(),
                momoPaymentProperties.getSecretKey())) {
            throw new BusinessException(PaymentConstant.INVALID_MOMO_SIGNATURE);
        }

        Payment payment = paymentRepository.findByOrderIdAndRequestId(
                request.getOrderId(),
                request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(PaymentConstant.PAYMENT_NOT_FOUND));

        if (request.getAmount() == null || Math.round(payment.getAmount()) != request.getAmount()) {
            throw new BusinessException(PaymentConstant.PAYMENT_AMOUNT_MISMATCH);
        }

        Invoice invoice = payment.getInvoice();
        Appointment appointment = invoice.getAppointment();
        if (invoice.getStatus() == InvoiceStatus.CANCELLED || appointment.getStatus() == AppointmentStatus.CANCELLED) {
            if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            }
            return;
        }

        if (request.getResultCode() != null && request.getResultCode() == MOMO_SUCCESS_CODE) {
            payment.setStatus(PaymentStatus.PAID);
            invoice.setStatus(InvoiceStatus.PAID);
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            invoiceRepository.save(invoice);
        } else if (payment.getStatus() != PaymentStatus.PAID) {
            payment.setStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
    }

    private String buildRawSignature(MomoIpnRequestDto request) {
        return "accessKey=" + momoPaymentProperties.getAccessKey()
                + "&amount=" + request.getAmount()
                + "&extraData=" + nullToEmpty(request.getExtraData())
                + "&message=" + nullToEmpty(request.getMessage())
                + "&orderId=" + nullToEmpty(request.getOrderId())
                + "&orderInfo=" + nullToEmpty(request.getOrderInfo())
                + "&orderType=" + nullToEmpty(request.getOrderType())
                + "&partnerCode=" + nullToEmpty(request.getPartnerCode())
                + "&payType=" + nullToEmpty(request.getPayType())
                + "&requestId=" + nullToEmpty(request.getRequestId())
                + "&responseTime=" + request.getResponseTime()
                + "&resultCode=" + request.getResultCode()
                + "&transId=" + request.getTransId();
    }

    private String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
