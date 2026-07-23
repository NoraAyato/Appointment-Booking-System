package com.abs.app.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.abs.app.domain.entity.enums.StaffShiftStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "staff_shifts")
public class StaffShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffShiftStatus status = StaffShiftStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User staff;

}
