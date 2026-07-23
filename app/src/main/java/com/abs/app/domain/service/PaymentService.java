package com.abs.app.domain.service;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.PaymentConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.enums.InvoiceStatus;

@Service
public class PaymentService {
    public long toMomoAmount(Double amount) {
        if (amount == null || amount <= 0D) {
            throw new BusinessException(PaymentConstant.INVALID_PAYMENT_AMOUNT);
        }
        return Math.round(amount);
    }

    public void validateInvoiceStatus(Invoice invoice) {
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessException(PaymentConstant.INVOICE_ALREADY_PAID);
        }
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BusinessException(PaymentConstant.INVOICE_ALREADY_CANCELLED);
        }
    }
}
