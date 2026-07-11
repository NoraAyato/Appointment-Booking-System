package com.abs.app.application.user.service.query;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailableStaffForServiceQuery {
    private String serviceId;
    private LocalDate date;
    private LocalTime time;
}
