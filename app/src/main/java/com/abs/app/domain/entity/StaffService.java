package com.abs.app.domain.entity;

import com.abs.app.domain.entity.enums.StaffServiceStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "staff_services")
public class StaffService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    StaffServiceStatus status = StaffServiceStatus.ACTIVE;
}
