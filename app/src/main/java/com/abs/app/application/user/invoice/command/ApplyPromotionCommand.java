package com.abs.app.application.user.invoice.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPromotionCommand {
    private String customerId;
    private String invoiceId;
    private String promotionCode;
}
