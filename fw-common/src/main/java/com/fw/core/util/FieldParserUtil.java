package com.fw.core.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public final class FieldParserUtil {

    private static Set<String> datePatternSet = null;

    private FieldParserUtil() {

    }

    private static Set<String> initDatePatternSet() {
        if (null == datePatternSet) {
            datePatternSet = new HashSet<>();
            datePatternSet.add("dd/MM/yyyy");
            datePatternSet.add("EEE MMM dd HH:mm:ss z yyyy");
            datePatternSet.add("EEE MMM dd HH:mm:ss SSS yyyy");
            datePatternSet.add("MM/dd/yyyy");
            datePatternSet.add("m/d/yyyy");
            datePatternSet.add("yyyyMMdd");
            datePatternSet.add("yyyy-MM-dd");

            datePatternSet.add("yyyy-MM-dd'T'HH:mm:ss'Z'");
            datePatternSet.add("yyyy-MM-dd'T'HH:mm:ssZ");
            datePatternSet.add("yyyy-MM-dd'T'HH:mm:ss");
            datePatternSet.add("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            datePatternSet.add("yyyy-MM-dd HH:mm:ss");
            datePatternSet.add("MM/dd/yyyy HH:mm:ss");
            datePatternSet.add("MM/dd/yyyy'T'HH:mm:ss.SSS'Z'");
            datePatternSet.add("MM/dd/yyyy'T'HH:mm:ss.SSSZ");
            datePatternSet.add("MM/dd/yyyy'T'HH:mm:ss.SSS");
            datePatternSet.add("MM/dd/yyyy'T'HH:mm:ssZ");
            datePatternSet.add("MM/dd/yyyy'T'HH:mm:ss");
            datePatternSet.add("yyyy:MM:dd HH:mm:ss");
        }

        return datePatternSet;
    }

    public static Byte parseByte(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }
        try {
            return Byte.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseByte] illegal input=" + value, e);
        }
    }

    public static Boolean parseBoolean(String value) {
        if (CommonUtil.isNullOrEmpty(value)) {
            return Boolean.FALSE;
        }
        if ("Y".equalsIgnoreCase(value) ||
                "YES".equalsIgnoreCase(value) ||
                "1".equalsIgnoreCase(value) ||
                Boolean.TRUE.toString().equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static Integer parseInt(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }
        try {
            value = value.replaceAll("\\s+","");
            return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).intValueExact();
        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseInt] illegal input. value=" + value, e);
        }
    }

    public static Short parseShort(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }
        try {
            return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).shortValueExact();
        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseShort] illegal input. value=" + value, e);
        }
    }

    public static Long parseLong(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }
        try {
            return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).longValueExact();

        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseLong] illegal input. value=" + value, e);
        }
    }

    private static boolean trimAndVerifyValue(String value) {
        value = value.trim();
        return NumberUtils.isCreatable(value);
    }

    public static Float parseFloat(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseFloat] illegal input. value=" + value, e);
        }
    }

    public static Double parseDouble(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }

        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseDouble]illegal input. value=" + value, e);
        }
    }

    public static BigDecimal parseBigDecimal(Object input) {
        String value = input.toString();
        if (!trimAndVerifyValue(value)) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("[parseBigDecimal]illegal input. value=" + value, e);
        }
    }

    public static Instant parseInstant(String value) {
        if (CommonUtil.isNullOrEmpty(value)) {
            return null;
        }

        try {
            if (NumberUtils.isCreatable(value)) {
                double doubleValue = Double.parseDouble(value.trim());
                Date date = DateUtil.getJavaDate(doubleValue);
                return date.toInstant();
            } else {
                return parseInstantFromString(value);
            }
        } catch (NumberFormatException e) {
            return parseInstantFromString(value);
        }
    }

    public static Instant parseInstantFromString(String value) {
        if (CommonUtil.isNullOrEmpty(value)) {
            return null;
        }
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
        initDatePatternSet().forEach(pattern -> dateTimeFormatterBuilder.appendOptional(DateTimeFormatter.ofPattern(pattern)));

        DateTimeFormatter dfs = dateTimeFormatterBuilder.toFormatter();
        LocalDate localDate = LocalDate.parse(value, dfs);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return zdt.toInstant();
    }

    public static Date parseDate(Object value) {
        try {
            return (Date) value;
        } catch (Exception ex) {
            return DateUtils.str2Date(value.toString());
        }
    }

    public static String parseString(Object value) {
        if (value == null || value.toString().equals("null")) {
            return null;
        }
        return String.valueOf(value);
    }

    public static String parseStringNoDecimal(Object value) {
        // Nếu object value có dạng số, chuyển sang kiểu int và sau đó chuyển sang String để bỏ trường hợp .0
        if (NumberUtils.isCreatable(value.toString())) {
            double doubleValue = Double.parseDouble(value.toString());
            long longValue = Math.round(doubleValue);
            return String.valueOf(longValue);
        } else {
            return String.valueOf(value);
        }
    }

}
