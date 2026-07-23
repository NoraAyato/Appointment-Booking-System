package com.abs.app.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.abs.app.domain.entity.enums.BlockedSlotStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "blocked_slots")
public class BlockedSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocked_date")
    private LocalDate blockedDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BlockedSlotStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User staff;
}
