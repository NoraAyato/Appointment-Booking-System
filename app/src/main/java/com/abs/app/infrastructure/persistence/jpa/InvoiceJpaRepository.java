package com.abs.app.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abs.app.domain.entity.Invoice;
import com.abs.app.domain.entity.enums.InvoiceStatus;

public interface InvoiceJpaRepository extends JpaRepository<Invoice, String> {
    @Query("""
            SELECT DISTINCT invoice
            FROM Invoice invoice
            JOIN FETCH invoice.appointment appointment
            JOIN FETCH appointment.customer customer
            LEFT JOIN FETCH invoice.promotion promotion
            LEFT JOIN FETCH appointment.appointmentDetails detail
            LEFT JOIN FETCH detail.service service
            LEFT JOIN FETCH service.category category
            LEFT JOIN FETCH detail.staff staff
            WHERE invoice.id = :invoiceId
            AND customer.userId = :customerId
            """)
    Optional<Invoice> findByIdAndCustomerId(
            @Param("invoiceId") String invoiceId,
            @Param("customerId") String customerId);

    @Query("""
            SELECT invoice.id
            FROM Invoice invoice
            WHERE invoice.status = :unpaidStatus
            AND invoice.createdAt <= :expiredBefore
            ORDER BY invoice.createdAt ASC
            """)
    List<String> findExpiredUnpaidInvoiceIds(
            @Param("unpaidStatus") InvoiceStatus unpaidStatus,
            @Param("expiredBefore") LocalDateTime expiredBefore,
            Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE Invoice invoice
            SET invoice.status = :cancelledStatus
            WHERE invoice.id IN :invoiceIds
            AND invoice.status = :unpaidStatus
            """)
    int cancelUnpaidInvoicesByIds(
            @Param("invoiceIds") List<String> invoiceIds,
            @Param("unpaidStatus") InvoiceStatus unpaidStatus,
            @Param("cancelledStatus") InvoiceStatus cancelledStatus);

    @Query("""
            SELECT COALESCE(SUM(invoice.amount), 0)
            FROM Invoice invoice
            WHERE invoice.status = :status
            AND invoice.createdAt >= :startAt
            AND invoice.createdAt < :endAt
            """)
    Double sumAmountByStatusAndCreatedAtBetween(
            @Param("status") InvoiceStatus status,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);

    long countByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            InvoiceStatus status,
            LocalDateTime startAt,
            LocalDateTime endAt);

    @Query(value = """
            SELECT DATE(i.created_at) AS revenue_date,
                   COALESCE(SUM(i.amount), 0) AS revenue,
                   COUNT(i.invoice_id) AS invoice_count
            FROM invoices i
            WHERE i.status = :status
            AND i.created_at >= :startAt
            AND i.created_at < :endAt
            GROUP BY DATE(i.created_at)
            ORDER BY revenue_date ASC
            """, nativeQuery = true)
    List<Object[]> getDailyRevenue(
            @Param("status") String status,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);
}
