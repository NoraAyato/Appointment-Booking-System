package com.abs.app.presentation.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abs.app.application.user.invoice.dto.InvoiceResponseDto;
import com.abs.app.application.user.invoice.query.GetInvoiceByIdQuery;
import com.abs.app.application.user.invoice.query.GetInvoiceByIdQueryHandler;
import com.abs.app.common.constant.InvoiceConstant;
import com.abs.app.common.response.ApiResponse;
import com.abs.app.infrastructure.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class InvoiceController {
    private final GetInvoiceByIdQueryHandler getInvoiceByIdQueryHandler;

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
}
