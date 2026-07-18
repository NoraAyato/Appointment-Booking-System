package com.abs.app.application.user.payment.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.application.user.payment.dto.CreateMomoPaymentResponseDto;
import com.abs.app.common.constant.InvoiceConstant;
import com.abs.app.common.constant.PaymentConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.common.util.GenerateIdUtil;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.PaymentMethod;
import com.abs.app.domain.entity.enums.PaymentStatus;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.PaymentRepository;
import com.abs.app.infrastructure.mapper.PaymentMapper;
import com.abs.app.infrastructure.payment.momo.MomoPaymentClient;
import com.abs.app.infrastructure.payment.momo.dto.MomoCreatePaymentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateMomoPaymentCommandHandler {
    private static final int MOMO_SUCCESS_CODE = 0;

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final MomoPaymentClient momoPaymentClient;

    @Transactional
    public CreateMomoPaymentResponseDto handle(CreateMomoPaymentCommand command) {
        Invoice invoice = invoiceRepository.findByIdAndCustomerId(
                command.getInvoiceId(),
                command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceConstant.NOT_FOUND));

        if (invoice.getStatus() != InvoiceStatus.UNPAID) {
            throw new BusinessException(PaymentConstant.INVOICE_ALREADY_PAID);
        }

        long amount = toMomoAmount(invoice.getAmount());
        Payment payment = createPendingMomoPayment(invoice, amount);
        String orderInfo = "Thanh toan hoa don " + invoice.getId();

        MomoCreatePaymentResponse momoResponse = momoPaymentClient.createPayment(
                amount,
                payment.getOrderId(),
                payment.getRequestId(),
                orderInfo);
        if (momoResponse == null || momoResponse.getResultCode() == null
                || momoResponse.getResultCode() != MOMO_SUCCESS_CODE) {
            throw new BusinessException(PaymentConstant.MOMO_CREATE_PAYMENT_FAILED);
        }

        payment.setDescription(orderInfo);
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentMapper.toCreateMomoPaymentResponse(savedPayment, momoResponse);
    }

    private long toMomoAmount(Double amount) {
        if (amount == null || amount <= 0D) {
            throw new BusinessException(PaymentConstant.INVALID_PAYMENT_AMOUNT);
        }
        return Math.round(amount);
    }

    private Payment createPendingMomoPayment(Invoice invoice, long amount) {
        Payment payment = new Payment();
        payment.setId(GenerateIdUtil.GenerateId(
                PaymentConstant.SALT_TAG,
                PaymentConstant.STRING_LIMIT));
        payment.setAmount((double) amount);
        payment.setPaymentMethod(PaymentMethod.MOMO);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setInvoice(invoice);
        payment.setOrderId(payment.getId());
        payment.setRequestId(GenerateIdUtil.GenerateId(
                PaymentConstant.REQUEST_SALT_TAG,
                PaymentConstant.STRING_LIMIT));
        return payment;
    }
}
