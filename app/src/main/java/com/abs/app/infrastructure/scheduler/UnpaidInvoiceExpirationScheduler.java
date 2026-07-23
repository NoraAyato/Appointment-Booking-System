package com.abs.app.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.abs.app.application.user.payment.command.ExpireUnpaidInvoicesCommandHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnpaidInvoiceExpirationScheduler {
    private final ExpireUnpaidInvoicesCommandHandler expireUnpaidInvoicesCommandHandler;

    @Scheduled(
            fixedDelayString = "${payment.expiration.fixed-delay-ms:60000}",
            initialDelayString = "${payment.expiration.initial-delay-ms:60000}")
    public void expireUnpaidInvoices() {
        int expiredInvoices = expireUnpaidInvoicesCommandHandler.handle();
        if (expiredInvoices > 0) {
            log.info("Expired {} unpaid invoices", expiredInvoices);
        }
    }
}
