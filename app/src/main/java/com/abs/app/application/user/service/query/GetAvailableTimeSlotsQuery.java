package com.abs.app.application.user.service.query;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailableTimeSlotsQuery {
    private String serviceId;
    private LocalDate date;
}
