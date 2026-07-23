package com.abs.app.application.user.service.query;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetServiceDataQuery {
    private String keyWord;
    private String categoryId;
    private LocalDate date;
    private LocalTime time;
    private int page;
    private int limit;
}
