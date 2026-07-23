package com.abs.app.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abs.app.domain.entity.Payment;
import com.abs.app.domain.entity.enums.PaymentStatus;

@Repository
public interface PaymentJpaRepository extends JpaRepository<Payment, String> {
    @Query("""
            SELECT payment
            FROM Payment payment
            JOIN FETCH payment.invoice invoice
            JOIN FETCH invoice.appointment appointment
            JOIN FETCH appointment.customer customer
            WHERE payment.orderId = :orderId
            AND payment.requestId = :requestId
            """)
    Optional<Payment> findByOrderIdAndRequestId(
            @Param("orderId") String orderId,
            @Param("requestId") String requestId);

    @Query("""
            SELECT payment
            FROM Payment payment
            JOIN FETCH payment.invoice invoice
            JOIN FETCH invoice.appointment appointment
            JOIN FETCH appointment.customer customer
            WHERE payment.id = :paymentId
            AND customer.userId = :customerId
            """)
    Optional<Payment> findByIdAndCustomerId(
            @Param("paymentId") String paymentId,
            @Param("customerId") String customerId);

    @Query("""
            SELECT payment
            FROM Payment payment
            JOIN FETCH payment.invoice invoice
            JOIN FETCH invoice.appointment appointment
            JOIN FETCH appointment.customer customer
            WHERE invoice.id = :invoiceId
            AND customer.userId = :customerId
            ORDER BY payment.paymentDate DESC
            """)
    List<Payment> findByInvoiceIdAndCustomerIdOrderByPaymentDateDesc(
            @Param("invoiceId") String invoiceId,
            @Param("customerId") String customerId);

    @Query("""
            SELECT payment
            FROM Payment payment
            JOIN FETCH payment.invoice invoice
            WHERE invoice.id IN :invoiceIds
            ORDER BY payment.paymentDate DESC, payment.id DESC
            """)
    List<Payment> findByInvoiceIdsOrderByPaymentDateDesc(
            @Param("invoiceIds") List<String> invoiceIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE Payment payment
            SET payment.status = :failedStatus
            WHERE payment.status = :pendingStatus
            AND payment.invoice.id IN :invoiceIds
            """)
    int failPendingPaymentsByInvoiceIds(
            @Param("invoiceIds") List<String> invoiceIds,
            @Param("pendingStatus") PaymentStatus pendingStatus,
            @Param("failedStatus") PaymentStatus failedStatus);
}
