package com.abs.app.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.abs.app.domain.entity.enums.ReviewsStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Reviews {

    @Id
    @Column(name = "reviews_id", columnDefinition = "varchar(20)")
    private String id;

    private String picture;

    private String description;

    @Column(name = "service_score")
    private BigDecimal serviceScore;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReviewsStatus status = ReviewsStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

}
