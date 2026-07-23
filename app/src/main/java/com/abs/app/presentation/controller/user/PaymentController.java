package com.abs.app.presentation.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.payment.command.CreateMomoPaymentCommand;
import com.abs.app.application.user.payment.command.CreateMomoPaymentCommandHandler;
import com.abs.app.application.user.payment.command.HandleMomoIpnCommandHandler;
import com.abs.app.application.user.payment.dto.CreateMomoPaymentResponseDto;
import com.abs.app.application.user.payment.dto.MomoIpnRequestDto;
import com.abs.app.application.user.payment.dto.PaymentStatusResponseDto;
import com.abs.app.application.user.payment.query.GetLatestPaymentByInvoiceQuery;
import com.abs.app.application.user.payment.query.GetPaymentStatusQuery;
import com.abs.app.application.user.payment.query.GetPaymentStatusQueryHandler;
import com.abs.app.common.constant.PaymentConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final CreateMomoPaymentCommandHandler createMomoPaymentCommandHandler;
    private final HandleMomoIpnCommandHandler handleMomoIpnCommandHandler;
    private final GetPaymentStatusQueryHandler getPaymentStatusQueryHandler;

    @PostMapping("/momo/{invoiceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CreateMomoPaymentResponseDto>> createMomoPayment(
            @PathVariable String invoiceId) {
        String userId = SecurityUtils.getCurrentUserId();
        CreateMomoPaymentResponseDto payment = createMomoPaymentCommandHandler.handle(
                new CreateMomoPaymentCommand(userId, invoiceId));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                PaymentConstant.CREATE_MOMO_SUCCESS,
                payment));
    }

    @PostMapping("/momo/ipn")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Void>> handleMomoIpn(@RequestBody MomoIpnRequestDto request) {
        handleMomoIpnCommandHandler.handle(request);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                PaymentConstant.HANDLE_MOMO_IPN_SUCCESS,
                null));
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentStatusResponseDto>> getPaymentStatus(@PathVariable String paymentId) {
        String userId = SecurityUtils.getCurrentUserId();
        PaymentStatusResponseDto payment = getPaymentStatusQueryHandler.handle(
                new GetPaymentStatusQuery(userId, paymentId));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                PaymentConstant.GET_PAYMENT_SUCCESS,
                payment));
    }

    @GetMapping("/invoice/{invoiceId}/latest")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentStatusResponseDto>> getLatestPaymentByInvoice(
            @PathVariable String invoiceId) {
        String userId = SecurityUtils.getCurrentUserId();
        PaymentStatusResponseDto payment = getPaymentStatusQueryHandler.handle(
                new GetLatestPaymentByInvoiceQuery(userId, invoiceId));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                PaymentConstant.GET_PAYMENT_SUCCESS,
                payment));
    }
}
