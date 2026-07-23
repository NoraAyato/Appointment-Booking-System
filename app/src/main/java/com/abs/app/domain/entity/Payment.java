package com.abs.app.domain.entity;

import java.time.LocalDateTime;

import com.abs.app.domain.entity.enums.PaymentMethod;
import com.abs.app.domain.entity.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false, columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.CASH; // e.g., Credit Card, PayPal, Cash

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.UNPAID;

    @Column(name = "order_id", columnDefinition = "VARCHAR(50)")
    private String orderId;

    @Column(name = "request_id", columnDefinition = "VARCHAR(50)")
    private String requestId;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
