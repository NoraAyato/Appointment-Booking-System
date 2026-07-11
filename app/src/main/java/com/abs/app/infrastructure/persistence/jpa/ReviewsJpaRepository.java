package com.abs.app.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abs.app.domain.entity.Reviews;
import com.abs.app.domain.entity.enums.ReviewsStatus;

import java.util.List;

public interface ReviewsJpaRepository extends JpaRepository<Reviews, String> {
        @EntityGraph(attributePaths = { "appointment", "appointment.customer" })
        @Query("""
                        SELECT r
                        FROM Reviews r
                        WHERE (:keyword IS NULL OR :keyword = ''
                            OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                        AND (:status IS NULL OR r.status = :status)
                        """)
        Page<Reviews> search(
                        @Param("keyword") String keyword,
                        @Param("status") ReviewsStatus status,
                        Pageable pageable);

        @EntityGraph(attributePaths = { "appointment", "appointment.customer" })
        @Query(value = """
                        SELECT DISTINCT r
                        FROM Reviews r
                        JOIN r.appointment a
                        JOIN a.appointmentDetails ad
                        WHERE ad.service.id = :serviceId
                        ORDER BY r.createAt DESC
                        """, countQuery = """
                        SELECT COUNT(DISTINCT r.id)
                        FROM Reviews r
                        JOIN r.appointment a
                        JOIN a.appointmentDetails ad
                        WHERE ad.service.id = :serviceId
                        """)
        Page<Reviews> findByServiceId(@Param("serviceId") String serviceId, Pageable pageable);

        @EntityGraph(attributePaths = { "appointment", "appointment.customer" })
        @Query(value = """
                        SELECT DISTINCT r
                        FROM Reviews r
                        JOIN r.appointment a
                        JOIN a.appointmentDetails ad
                        WHERE ad.service.id = :serviceId
                        AND r.status = :status
                        ORDER BY r.createAt DESC
                        """, countQuery = """
                        SELECT COUNT(DISTINCT r.id)
                        FROM Reviews r
                        JOIN r.appointment a
                        JOIN a.appointmentDetails ad
                        WHERE ad.service.id = :serviceId
                        AND r.status = :status
                        """)
        Page<Reviews> findByServiceIdAndStatus(
                        @Param("serviceId") String serviceId,
                        @Param("status") ReviewsStatus status,
                        Pageable pageable);

        @Query("""
                        SELECT AVG(r.serviceScore)
                        FROM Reviews r
                        JOIN r.appointment a
                        JOIN a.appointmentDetails ad
                        WHERE ad.service.id = :serviceId
                        AND r.status = :status
                        """)
        Double findAverageRatingByServiceIdAndStatus(
                        @Param("serviceId") String serviceId,
                        @Param("status") ReviewsStatus status);

        @Query("""
                        SELECT COUNT(DISTINCT r.id)
                        FROM Reviews r
                        JOIN r.appointment a
                        JOIN a.appointmentDetails ad
                        WHERE ad.service.id = :serviceId
                        AND r.status = :status
                        """)
        long countByServiceIdAndStatus(
                        @Param("serviceId") String serviceId,
                        @Param("status") ReviewsStatus status);

        @Query(value = """
                        SELECT CAST(ROUND(r.service_score) AS SIGNED) AS rating_bucket,
                               COUNT(DISTINCT r.reviews_id) AS review_count
                        FROM reviews r
                        JOIN appointments a ON a.appointment_id = r.appointment_id
                        JOIN appointment_details ad ON ad.appointment_id = a.appointment_id
                        WHERE ad.service_id = :serviceId
                        AND r.status = :status
                        GROUP BY CAST(ROUND(r.service_score) AS SIGNED)
                        """, nativeQuery = true)
        List<Object[]> findRatingDistributionByServiceIdAndStatus(
                        @Param("serviceId") String serviceId,
                        @Param("status") String status);

        default List<Object[]> findRatingDistributionByServiceIdAndStatus(String serviceId, ReviewsStatus status) {
                return findRatingDistributionByServiceIdAndStatus(serviceId, status.name());
        }

        @EntityGraph(attributePaths = {
                        "appointment",
                        "appointment.customer",
                        "appointment.appointmentDetails",
                        "appointment.appointmentDetails.service"
        })
        @Query("""
                        SELECT r
                        FROM Reviews r
                        WHERE r.status = :status
                        ORDER BY r.serviceScore DESC, r.createAt DESC
                        """)
        List<Reviews> findTopByStatus(
                        @Param("status") ReviewsStatus status,
                        Pageable pageable);
}
