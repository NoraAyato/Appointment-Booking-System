package com.abs.app.application.user.invoice.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetInvoiceByIdQuery {
    private String customerId;
    private String invoiceId;
}
