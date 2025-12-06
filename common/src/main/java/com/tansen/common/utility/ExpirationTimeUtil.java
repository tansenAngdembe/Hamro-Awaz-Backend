package com.tansen.common.utility;

import java.util.Calendar;
import java.util.Date;

public class ExpirationTimeUtil {
    public static Date getExpirationTime(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
