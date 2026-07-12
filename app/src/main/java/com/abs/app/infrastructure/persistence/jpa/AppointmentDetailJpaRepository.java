package com.abs.app.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query(value = """
            SELECT DATE(ad.start_time) AS appointment_date,
                   COUNT(DISTINCT a.appointment_id) AS total_count,
                   COUNT(DISTINCT CASE WHEN a.status = :completedStatus THEN a.appointment_id END) AS completed_count,
                   COUNT(DISTINCT CASE WHEN a.status = :cancelledStatus THEN a.appointment_id END) AS cancelled_count
            FROM appointment_details ad
            JOIN appointments a ON a.appointment_id = ad.appointment_id
            WHERE ad.start_time >= :startAt
            AND ad.start_time < :endAt
            GROUP BY DATE(ad.start_time)
            ORDER BY appointment_date ASC
            """, nativeQuery = true)
    List<Object[]> countDailyAppointments(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("completedStatus") String completedStatus,
            @Param("cancelledStatus") String cancelledStatus);

    default List<Object[]> countDailyAppointments(
            LocalDateTime startAt,
            LocalDateTime endAt,
            AppointmentStatus completedStatus,
            AppointmentStatus cancelledStatus) {
        return countDailyAppointments(startAt, endAt, completedStatus.name(), cancelledStatus.name());
    }

    @Query(value = """
            SELECT s.service_id,
                   s.service_name,
                   COUNT(ad.id) AS booking_count,
                   COALESCE(SUM(CASE WHEN a.status = 'COMPLETED' THEN COALESCE(s.price, 0) * ad.quantity ELSE 0 END), 0) AS revenue,
                   COALESCE(AVG(CASE WHEN r.status = 'APPROVED' THEN r.service_score ELSE NULL END), 0) AS average_rating
            FROM appointment_details ad
            JOIN services s ON s.service_id = ad.service_id
            JOIN appointments a ON a.appointment_id = ad.appointment_id
            LEFT JOIN reviews r ON r.appointment_id = a.appointment_id
            WHERE ad.start_time >= :startAt
            AND ad.start_time < :endAt
            GROUP BY s.service_id, s.service_name
            ORDER BY booking_count DESC, revenue DESC, s.service_name ASC
            """, nativeQuery = true)
    List<Object[]> findTopServices(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            Pageable pageable);

    @Query(value = """
            SELECT staff.user_id,
                   TRIM(CONCAT(COALESCE(staff.first_name, ''), ' ', COALESCE(staff.last_name, ''))) AS staff_name,
                   COUNT(DISTINCT CASE WHEN a.status = :completedStatus THEN a.appointment_id END) AS completed_count,
                   COUNT(DISTINCT CASE WHEN a.status = :cancelledStatus THEN a.appointment_id END) AS cancelled_count,
                   COALESCE(AVG(CASE WHEN r.status = 'APPROVED' THEN r.service_score ELSE NULL END), 0) AS average_rating,
                   COALESCE((
                       SELECT SUM(TIMESTAMPDIFF(MINUTE, sh.start_time, sh.end_time)) / 60
                       FROM staff_shifts sh
                       WHERE sh.user_id = staff.user_id
                       AND sh.status = 'APPROVED'
                       AND sh.work_date >= :fromDate
                       AND sh.work_date <= :toDate
                   ), 0) AS total_working_hours
            FROM appointment_details ad
            JOIN users staff ON staff.user_id = ad.staff_id
            JOIN appointments a ON a.appointment_id = ad.appointment_id
            LEFT JOIN reviews r ON r.appointment_id = a.appointment_id
            WHERE ad.start_time >= :startAt
            AND ad.start_time < :endAt
            GROUP BY staff.user_id, staff.first_name, staff.last_name
            ORDER BY completed_count DESC, average_rating DESC, staff_name ASC
            """, nativeQuery = true)
    List<Object[]> findStaffPerformance(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("completedStatus") String completedStatus,
            @Param("cancelledStatus") String cancelledStatus,
            Pageable pageable);

    default List<Object[]> findStaffPerformance(
            LocalDateTime startAt,
            LocalDateTime endAt,
            LocalDate fromDate,
            LocalDate toDate,
            AppointmentStatus completedStatus,
            AppointmentStatus cancelledStatus,
            Pageable pageable) {
        return findStaffPerformance(
                startAt,
                endAt,
                fromDate,
                toDate,
                completedStatus.name(),
                cancelledStatus.name(),
                pageable);
    }

    @EntityGraph(attributePaths = {
            "appointment",
            "appointment.customer",
            "service",
            "staff"
    })
    @Query("""
            SELECT appointmentDetail
            FROM AppointmentDetail appointmentDetail
            JOIN appointmentDetail.appointment appointment
            WHERE appointmentDetail.startTime >= :fromTime
            AND appointment.status <> :excludedStatus
            ORDER BY appointmentDetail.startTime ASC
            """)
    List<AppointmentDetail> findUpcomingAppointments(
            @Param("fromTime") LocalDateTime fromTime,
            @Param("excludedStatus") AppointmentStatus excludedStatus,
            Pageable pageable);
}
