package com.abs.app.application.admin.dashboard.query;

import java.sql.Date;
import java.time.LocalDate;

class DashboardRowMapper {
    private DashboardRowMapper() {
    }

    static String stringValue(Object value) {
        return value != null ? value.toString() : "";
    }

    static long longValue(Object value) {
        return value != null ? ((Number) value).longValue() : 0L;
    }

    static double doubleValue(Object value) {
        return value != null ? ((Number) value).doubleValue() : 0D;
    }

    static LocalDate localDateValue(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        return LocalDate.parse(value.toString());
    }
}
