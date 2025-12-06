package com.tansen.common.utility;



import com.tansen.common.dto.SearchParam;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class SearchParamUtil {
    public static String getString(SearchParam searchParam, String keyName) {
        return (String) searchParam.getParam().get(keyName);
    }

    public static Date getDate(SearchParam searchParam, String keyName, String dateFormat) {
        String stringDate = getString(searchParam, keyName);
        if (stringDate != null && !stringDate.isBlank()) {
            Instant instant = Instant.parse(stringDate);

            Date date = Date.from(instant);

            return DateUtility.getDateFromDate(date, dateFormat);
        }
        return null;
    }

    public static Date getDateWithTime(SearchParam searchParam, String keyName, String dateFormat) {
        String stringDate = getString(searchParam, keyName);
        if (stringDate != null && !stringDate.isBlank()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                LocalDate localDate = LocalDate.parse(stringDate, formatter);
                LocalDateTime localDateTime;
                if ("endDate".equals(keyName)) {
                    localDateTime = localDate.atTime(23, 59, 59);
                } else {
                    localDateTime = localDate.atStartOfDay();
                }
                Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                return DateUtility.getDateFromDate(date, dateFormat);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + stringDate, e);
            }
        }
        return null;
    }
    public static BigDecimal getBigDecimal(SearchParam searchParam, String keyName) {
        Object value = searchParam.getParam().get(keyName);
        if (value == null) return null;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static LocalDateTime getDateTime(SearchParam searchParam, String keyName) {
        String stringDate = getString(searchParam, keyName);
        if (stringDate != null && !stringDate.isBlank()) {
            try {
                return LocalDateTime.parse(stringDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + stringDate, e);
            }
        }
        return null;
    }
}
