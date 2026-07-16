package com.abs.app.domain.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.abs.app.common.constant.InvoiceConstant;
import com.abs.app.common.exception.BusinessException;
import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.Promotion;
import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.enums.DiscountType;
import com.abs.app.domain.entity.enums.InvoiceStatus;
import com.abs.app.domain.entity.enums.PromotionStatus;

@Service
public class InvoicePricingService {
    public double applyPromotion(Invoice invoice, Promotion promotion, LocalDate currentDate) {
        if (invoice.getStatus() != InvoiceStatus.UNPAID) {
            throw new BusinessException(InvoiceConstant.CANNOT_APPLY_PROMOTION);
        }
        if (invoice.getPromotion() != null) {
            throw new BusinessException(InvoiceConstant.PROMOTION_ALREADY_APPLIED);
        }
        if (!isPromotionAvailable(promotion, currentDate)) {
            throw new BusinessException(InvoiceConstant.PROMOTION_NOT_AVAILABLE);
        }

        double subtotal = calculateSubtotal(invoice);
        double discountAmount = calculateDiscountAmount(subtotal, promotion);
        double appliedDiscountAmount = Math.min(discountAmount, subtotal);

        invoice.setPromotion(promotion);
        invoice.setAmount(Math.max(subtotal - appliedDiscountAmount, 0D));

        return appliedDiscountAmount;
    }

    private boolean isPromotionAvailable(Promotion promotion, LocalDate currentDate) {
        return promotion.getStatus() == PromotionStatus.ACTIVE
                && !promotion.getStartDate().isAfter(currentDate)
                && !promotion.getEndDate().isBefore(currentDate);
    }

    private double calculateSubtotal(Invoice invoice) {
        if (invoice.getAppointment() == null || invoice.getAppointment().getAppointmentDetails() == null) {
            return 0D;
        }

        return invoice.getAppointment().getAppointmentDetails().stream()
                .mapToDouble(this::calculateDetailAmount)
                .sum();
    }

    private double calculateDetailAmount(AppointmentDetail detail) {
        ServiceEntity service = detail.getService();
        double price = service != null && service.getPrice() != null ? service.getPrice() : 0D;
        return price * detail.getQuantity();
    }

    private double calculateDiscountAmount(double subtotal, Promotion promotion) {
        double promotionValue = promotion.getDiscountAmount() != null ? promotion.getDiscountAmount() : 0D;
        if (promotion.getDiscountType() == DiscountType.PERCENTAGE) {
            return subtotal * promotionValue / 100D;
        }

        return promotionValue;
    }
}
