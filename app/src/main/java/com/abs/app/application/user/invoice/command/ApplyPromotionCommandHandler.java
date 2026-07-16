package com.abs.app.application.user.invoice.command;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abs.app.common.constant.InvoiceConstant;
import com.abs.app.common.constant.PromotionConstant;
import com.abs.app.common.exception.ResourceNotFoundException;
import com.abs.app.application.user.invoice.dto.ApplyPromotionResponseDto;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.repository.InvoiceRepository;
import com.abs.app.domain.repository.PromotionRepository;
import com.abs.app.domain.service.InvoicePricingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplyPromotionCommandHandler {
    private final InvoiceRepository invoiceRepository;
    private final PromotionRepository promotionRepository;
    private final InvoicePricingService invoicePricingService;

    @Transactional
    public ApplyPromotionResponseDto handle(ApplyPromotionCommand command) {
        Invoice invoice = invoiceRepository.findByIdAndCustomerId(
                command.getInvoiceId(),
                command.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceConstant.NOT_FOUND));

        Promotion promotion = promotionRepository.findByCode(command.getPromotionCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException(PromotionConstant.NOT_EXIST));

        double discountValue = invoicePricingService.applyPromotion(invoice, promotion, LocalDate.now());
        invoiceRepository.save(invoice);

        return new ApplyPromotionResponseDto(
                promotion.getCode(),
                Double.toString(discountValue));
    }
}
