package com.abs.app.domain.entity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private Date workDate;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User staff;

}
