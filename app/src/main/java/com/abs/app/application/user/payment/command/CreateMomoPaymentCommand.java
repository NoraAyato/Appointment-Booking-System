package com.abs.app.application.user.payment.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMomoPaymentCommand {
    private String userId;
    private String invoiceId;
}
