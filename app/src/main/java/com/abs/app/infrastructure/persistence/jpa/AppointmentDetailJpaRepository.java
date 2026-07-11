package com.abs.app.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abs.app.domain.entity.AppointmentDetail;
import com.abs.app.domain.entity.enums.AppointmentStatus;

public interface AppointmentDetailJpaRepository extends JpaRepository<AppointmentDetail, Long> {

    @Query("""
            SELECT appointmentDetail.staff.userId, COUNT(appointmentDetail.id)
            FROM AppointmentDetail appointmentDetail
            JOIN appointmentDetail.appointment appointment
            WHERE appointmentDetail.staff.userId IN :staffIds
            AND appointment.status = :appointmentStatus
            GROUP BY appointmentDetail.staff.userId
            """)
    List<Object[]> countCompletedServicesByStaffIds(
            @Param("staffIds") List<String> staffIds,
            @Param("appointmentStatus") AppointmentStatus appointmentStatus);
}
