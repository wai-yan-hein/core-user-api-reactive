/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.user.api.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author WSwe
 */
@Slf4j
public class Util1 {
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    public static String isNull(String strValue, String value) {
        if (strValue == null) {
            return value;
        } else if (strValue.isEmpty()) {
            return value;
        } else {
            return strValue;
        }
    }

    public static String getString(Object value) {
        return value == null ? null : value.toString();
    }

    public static int getInteger(Object number) {
        int value = 0;
        if (number != null) {
            if (!number.toString().isEmpty()) {
                value = Integer.parseInt(number.toString());
            }
        }
        return value;
    }

    public static String toDateStr(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String strDate = null;

        try {
            strDate = formatter.format(date);
        } catch (Exception ex) {
            System.out.println("toDateStr Error : " + ex.getMessage());
        }

        return strDate;
    }

    public static Date getTodayDate() {
        return Calendar.getInstance().getTime();
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || obj.toString().isEmpty();
    }

    public static LocalDateTime toDateTime(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return LocalDateTime.of(date.toLocalDate(), LocalTime.of(now.getHour(), now.getMinute(), now.getSecond()));
    }

    public static Date toDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime toLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(str, formatter);
    }

    public static String getPassword() {
        return Util1.toDateStr(Util1.getTodayDate(), "yyyyMMdd");
    }

    public static String cleanStr(String str) {
        if (str != null) {
            return str.replaceAll(" ", "").toLowerCase();
        }
        return "";
    }

    public static boolean getBoolean(Object obj) {
        return obj != null && (obj.toString().equals("1") || obj.toString().equalsIgnoreCase("true"));
    }

    public static ZonedDateTime toZonedDateTime(LocalDateTime ldt) {
        if (ldt != null) {
            return ldt.atZone(ZoneId.systemDefault());
        }
        return null;
    }
}
