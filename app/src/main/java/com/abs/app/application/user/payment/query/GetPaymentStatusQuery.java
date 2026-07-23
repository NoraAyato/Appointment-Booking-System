package com.abs.app.application.user.payment.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentStatusQuery {
    private String userId;
    private String paymentId;
}
