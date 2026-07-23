package com.abs.app.application.user.appointment.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBookingHistoryQuery {
    private String customerId;
    private int page;
    private int limit;
}
