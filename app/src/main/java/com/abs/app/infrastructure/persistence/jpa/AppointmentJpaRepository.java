package com.abs.app.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abs.app.domain.entity.Appointment;

public interface AppointmentJpaRepository extends JpaRepository<Appointment, String> {
    @Query("""
            SELECT COUNT(DISTINCT appointment.id)
            FROM AppointmentDetail appointmentDetail
            JOIN appointmentDetail.appointment appointment
            WHERE appointmentDetail.startTime >= :startAt
            AND appointmentDetail.startTime < :endAt
            """)
    long countAppointmentsBetween(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);

    @Query("""
            SELECT appointment.status, COUNT(DISTINCT appointment.id)
            FROM AppointmentDetail appointmentDetail
            JOIN appointmentDetail.appointment appointment
            WHERE appointmentDetail.startTime >= :startAt
            AND appointmentDetail.startTime < :endAt
            GROUP BY appointment.status
            """)
    List<Object[]> countAppointmentsByStatus(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);
}
