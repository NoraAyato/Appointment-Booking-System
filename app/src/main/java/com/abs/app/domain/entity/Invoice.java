package com.abs.app.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.abs.app.domain.entity.enums.InvoiceStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @Column(name = "invoice_id", nullable = false, columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status; // PAID, UNPAID, CANCELLED

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
