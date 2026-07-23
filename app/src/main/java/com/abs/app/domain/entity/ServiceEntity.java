package com.abs.app.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.abs.app.domain.entity.enums.ServiceStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @Column(name = "service_id", columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "service_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    private Double price;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status = ServiceStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffService> staffServices = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceImage> serviceImage = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentDetail> appointmentDetails = new ArrayList<>();
}
