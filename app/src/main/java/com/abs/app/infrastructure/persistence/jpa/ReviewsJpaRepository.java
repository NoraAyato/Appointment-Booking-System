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

    @Query("""
            SELECT appointmentDetail.staff.userId, AVG(review.serviceScore)
            FROM Reviews review
            JOIN review.appointment appointment
            JOIN appointment.appointmentDetails appointmentDetail
            WHERE appointmentDetail.staff.userId IN :staffIds
            AND review.status = :reviewStatus
            GROUP BY appointmentDetail.staff.userId
            """)
    List<Object[]> findAverageRatingsByStaffIds(
            @Param("staffIds") List<String> staffIds,
            @Param("reviewStatus") ReviewsStatus reviewStatus);
}
