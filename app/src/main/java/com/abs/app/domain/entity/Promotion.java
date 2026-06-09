package com.abs.app.domain.entity;

import java.time.LocalDate;
import java.util.List;

import com.abs.app.domain.entity.enums.DiscountType;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "promotions")
public class Promotion {
    @Id
    @Column(name = "promotion_id", nullable = false, columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;
}
