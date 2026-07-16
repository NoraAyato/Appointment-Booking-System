package com.abs.app.presentation.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.invoice.command.ApplyPromotionCommand;
import com.abs.app.application.user.invoice.command.ApplyPromotionCommandHandler;
import com.abs.app.application.user.invoice.dto.ApplyPromotionRequestDto;
import com.abs.app.application.user.invoice.dto.ApplyPromotionResponseDto;
import com.abs.app.application.user.invoice.dto.InvoiceResponseDto;
import com.abs.app.application.user.invoice.query.GetInvoiceByIdQuery;
import com.abs.app.application.user.invoice.query.GetInvoiceByIdQueryHandler;
import com.abs.app.common.constant.InvoiceConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InvoiceController {
    private final GetInvoiceByIdQueryHandler getInvoiceByIdQueryHandler;
    private final ApplyPromotionCommandHandler applyPromotionCommandHandler;

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceResponseDto>> getInvoiceById(@PathVariable String invoiceId) {
        String customerId = SecurityUtils.getCurrentUserId();
        InvoiceResponseDto invoice = getInvoiceByIdQueryHandler.handle(new GetInvoiceByIdQuery(
                customerId,
                invoiceId));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                InvoiceConstant.GET_SUCCESS,
                invoice));
    }

    @PutMapping("/apply-promotion/{invoiceId}")
    public ResponseEntity<ApiResponse<ApplyPromotionResponseDto>> applyPromotion(
            @Valid @RequestBody ApplyPromotionRequestDto request,
            @PathVariable String invoiceId) {
        String customerId = SecurityUtils.getCurrentUserId();
        ApplyPromotionResponseDto promotion = applyPromotionCommandHandler.handle(new ApplyPromotionCommand(
                customerId,
                invoiceId,
                request.getPromotionCode()));

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                InvoiceConstant.APPLY_PROMOTION_SUCCESS,
                promotion));
    }
}
