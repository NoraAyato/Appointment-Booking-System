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
import com.abs.app.domain.service.PaymentService;
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
    private final PaymentService paymentService;

    @Transactional
    public CreateMomoPaymentResponseDto handle(CreateMomoPaymentCommand command) {
        Invoice invoice = invoiceRepository.findByIdAndCustomerId(
                command.getInvoiceId(),
                command.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceConstant.NOT_FOUND));

        paymentService.validateInvoiceStatus(invoice);
        long amount = paymentService.toMomoAmount(invoice.getAmount());
        Payment payment = PaymentMapper.createPendingMomoPayment(invoice, amount);
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
}
