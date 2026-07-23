package com.abs.app.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.abs.app.domain.entity.enums.AppointmentStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @Column(name = "appointment_id", nullable = false, columnDefinition = "VARCHAR(20)")
    private String id;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User customer;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Invoice invoice;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentDetail> appointmentDetails = new ArrayList<>();

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Reviews reviews;
}
