package com.abs.app.infrastructure.persistence.jpa;

import com.abs.app.domain.entity.ServiceEntity;
import com.abs.app.domain.entity.ServiceImage;
import com.abs.app.domain.entity.enums.ServiceStatus;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ServiceJpaRepository extends JpaRepository<ServiceEntity, String> {
    Optional<ServiceEntity> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndCategoryId(String name, String categoryId);
    Page<ServiceEntity> findByStatus(ServiceStatus status, Pageable pageable);
    Page<ServiceEntity> findByNameContainingIgnoreCaseAndStatus(String name, ServiceStatus status, Pageable pageable);
    Page<ServiceEntity> findByCategoryIdAndStatus(String categoryId, ServiceStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    @Query("""
            SELECT s
            FROM ServiceEntity s
            WHERE (:keyword IS NULL OR :keyword = ''
                OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:status IS NULL OR s.status = :status)
            AND (:categoryId IS NULL OR :categoryId = '' OR s.category.id = :categoryId)
            """)
    Page<ServiceEntity> search(
            @Param("keyword") String keyword,
            @Param("status") ServiceStatus status,
            @Param("categoryId") String categoryId,
            Pageable pageable);

    @Query(
            value = """
                    SELECT DISTINCT s.*
                    FROM services s
                    WHERE s.status = :serviceStatus
                    AND (:keyword IS NULL OR :keyword = ''
                        OR LOWER(s.service_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    AND (:categoryId IS NULL OR :categoryId = '' OR s.category_id = :categoryId)
                    AND (:date IS NULL OR EXISTS (
                        SELECT 1
                        FROM staff_services ss
                        JOIN users staff ON staff.user_id = ss.user_id
                        JOIN staff_shifts sh ON sh.user_id = staff.user_id
                        WHERE ss.service_id = s.service_id
                        AND ss.status = :staffServiceStatus
                        AND sh.status = :staffShiftStatus
                        AND sh.work_date = :date
                        AND (:time IS NULL OR (
                            sh.start_time <= :time
                            AND sh.end_time >= TIME(TIMESTAMPADD(MINUTE, s.duration_minutes, :requestedStartAt))
                            AND NOT EXISTS (
                                SELECT 1
                                FROM blocked_slots bs
                                WHERE bs.user_id = staff.user_id
                                AND bs.status = :blockedSlotStatus
                                AND bs.blocked_date = :date
                                AND bs.start_time < TIME(TIMESTAMPADD(MINUTE, s.duration_minutes, :requestedStartAt))
                                AND bs.end_time > :time
                            )
                            AND NOT EXISTS (
                                SELECT 1
                                FROM appointment_details ad
                                JOIN appointments a ON a.appointment_id = ad.appointment_id
                                WHERE ad.staff_id = staff.user_id
                                AND a.status <> :excludedAppointmentStatus
                                AND ad.start_time < TIMESTAMPADD(MINUTE, s.duration_minutes, :requestedStartAt)
                                AND ad.end_time > :requestedStartAt
                            )
                        ))
                    ))
                    ORDER BY s.service_name ASC
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT s.service_id)
                    FROM services s
                    WHERE s.status = :serviceStatus
                    AND (:keyword IS NULL OR :keyword = ''
                        OR LOWER(s.service_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    AND (:categoryId IS NULL OR :categoryId = '' OR s.category_id = :categoryId)
                    AND (:date IS NULL OR EXISTS (
                        SELECT 1
                        FROM staff_services ss
                        JOIN users staff ON staff.user_id = ss.user_id
                        JOIN staff_shifts sh ON sh.user_id = staff.user_id
                        WHERE ss.service_id = s.service_id
                        AND ss.status = :staffServiceStatus
                        AND sh.status = :staffShiftStatus
                        AND sh.work_date = :date
                        AND (:time IS NULL OR (
                            sh.start_time <= :time
                            AND sh.end_time >= TIME(TIMESTAMPADD(MINUTE, s.duration_minutes, :requestedStartAt))
                            AND NOT EXISTS (
                                SELECT 1
                                FROM blocked_slots bs
                                WHERE bs.user_id = staff.user_id
                                AND bs.status = :blockedSlotStatus
                                AND bs.blocked_date = :date
                                AND bs.start_time < TIME(TIMESTAMPADD(MINUTE, s.duration_minutes, :requestedStartAt))
                                AND bs.end_time > :time
                            )
                            AND NOT EXISTS (
                                SELECT 1
                                FROM appointment_details ad
                                JOIN appointments a ON a.appointment_id = ad.appointment_id
                                WHERE ad.staff_id = staff.user_id
                                AND a.status <> :excludedAppointmentStatus
                                AND ad.start_time < TIMESTAMPADD(MINUTE, s.duration_minutes, :requestedStartAt)
                                AND ad.end_time > :requestedStartAt
                            )
                        ))
                    ))
                    """,
            nativeQuery = true)
    Page<ServiceEntity> searchUserServices(
            @Param("keyword") String keyword,
            @Param("categoryId") String categoryId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("requestedStartAt") LocalDateTime requestedStartAt,
            @Param("serviceStatus") String serviceStatus,
            @Param("staffServiceStatus") String staffServiceStatus,
            @Param("staffShiftStatus") String staffShiftStatus,
            @Param("blockedSlotStatus") String blockedSlotStatus,
            @Param("excludedAppointmentStatus") String excludedAppointmentStatus,
            Pageable pageable);

    @Query("""
            SELECT si
            FROM ServiceImage si
            WHERE si.service.id IN :serviceIds
            ORDER BY si.isMainImage DESC, si.id ASC
            """)
    List<ServiceImage> findImagesByServiceIds(@Param("serviceIds") List<String> serviceIds);

    @Query("""
            SELECT ad.service.id, AVG(r.serviceScore)
            FROM Reviews r
            JOIN r.appointment a
            JOIN a.appointmentDetails ad
            WHERE ad.service.id IN :serviceIds
            AND r.status = com.abs.app.domain.entity.enums.ReviewsStatus.APPROVED
            GROUP BY ad.service.id
            """)
    List<Object[]> findAverageRatingsByServiceIds(@Param("serviceIds") List<String> serviceIds);

    @Query(
            value = """
                    SELECT s.*
                    FROM services s
                    JOIN (
                        SELECT ad.service_id, AVG(r.service_score) AS avg_score, COUNT(r.reviews_id) AS review_count
                        FROM appointment_details ad
                        JOIN appointments a ON a.appointment_id = ad.appointment_id
                        JOIN reviews r ON r.appointment_id = a.appointment_id
                        WHERE r.status = :reviewStatus
                        GROUP BY ad.service_id
                    ) scores ON scores.service_id = s.service_id
                    WHERE s.status = :serviceStatus
                    ORDER BY scores.avg_score DESC, scores.review_count DESC, s.service_name ASC
                    """,
            nativeQuery = true)
    List<ServiceEntity> findTopRatedServices(
            @Param("serviceStatus") String serviceStatus,
            @Param("reviewStatus") String reviewStatus,
            Pageable pageable);
}
