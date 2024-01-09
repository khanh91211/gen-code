package com.fw.core.util;

import com.fw.model.dto.exception.BusinessException;
import com.fw.model.enumeration.ErrorEnum;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private DateUtils() {
    }

    public static final String FORMAT_DDMMYYYY_HHMMSS = "dd/MM/yyyy hh:mm:ss";
    public static final String FORMAT_DDMMYYYY = "dd/MM/yyyy";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String F_YYYYMMDD = "yyyy-MM-dd";

    public static final String FORMAT_MMYYYY = "MM/yyyy";

    public static java.sql.Date currentDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    public static String convertPatternStringDate(String date, String parse, String format) {
        try {
            if (!CommonUtil.isNullOrEmpty(date)) {
                if (date.charAt(4) == format.charAt(4)) {
                    return date;
                }
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                SimpleDateFormat formatDate = new SimpleDateFormat(format);
                Date parseDate = sdf.parse(date);
                return formatDate.format(parseDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String date2Str(Date date) {
        if (date == null) {
            return null;
        }
        String format = F_YYYYMMDD;
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String date2StrFormat(Date date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date str2Date(String strDate) {
        Date result = null;
        if (StringUtils.isBlank(strDate) || strDate.equals("null")) {
            return result;
        }
        try {
            DateFormat df = null;
            if (strDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                df = new SimpleDateFormat("dd-MM-yyyy");
            } else if (strDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                df = new SimpleDateFormat(FORMAT_DDMMYYYY);
            } else if (strDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                df = new SimpleDateFormat(F_YYYYMMDD);
            } else if (strDate.matches("\\d{4}/\\d{2}/\\d{2}")) {
                df = new SimpleDateFormat("yyyy/MM/dd");
            } else if (strDate.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) {
                df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            } else if (strDate.matches("\\w{3} \\w{3} \\d{2} \\d{4} \\d{2}:\\d{2}:\\d{2} \\w{3}\\+\\w{4}")) {
                df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z");
            } else if (strDate.matches("\\w{3} \\w{3} \\d{2} \\d{4} \\d{2}:\\d{2}:\\d{2} \\w{3}")) {
                df = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z");
            } else if (strDate.matches("\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} GMT[+-]\\d{2}:\\d{2} \\d{4}$")) {
                df = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'XXX yyyy");
            } else {
                df = new SimpleDateFormat(FORMAT_DDMMYYYY);
            }
            result = df.parse(strDate.trim());
        } catch (ParseException e) {
            return null;
        }

        return result;
    }

    public static boolean isDateFormat(String strDate) {
        if (StringUtils.isBlank(strDate)) {
            return false;
        }
        if (strDate.matches("\\d{0,2}/\\d{0,2}/\\d{4}")) {
            return true;
        } else if (strDate.matches("\\d{0,2}-\\d{0,2}-\\d{4}")) {
            return true;
        } else if (strDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return true;
        } else if (strDate.matches("\\d{4}/\\d{2}/\\d{2}")) {
            return true;
        } else if (strDate.matches("\\w{3} \\w{3} \\d{2} \\d{4} \\d{2}:\\d{2}:\\d{2} \\w{3}\\+\\w{4}")) {
            return true;
        } else return strDate.matches("\\w{3} \\w{3} \\d{2} \\d{4} \\d{2}:\\d{2}:\\d{2} \\w{3}");
    }

    public static String getPreviousPeriod(String period) {
        String[] parts = period.split("[-/]");
        String a = parts[0];
        String b = parts[1];
        if (a.length() <= 2) {
            int tam = Integer.parseInt(a);
            if (tam <= 10) {
                if (tam == 1) {
                    a = String.valueOf(12);
                    b = String.valueOf(Integer.parseInt(b) - 1);
                } else {
                    a = "0" + (tam - 1);
                }
            } else {
                a = String.valueOf(tam - 1);
            }
        }
        period = a + "/" + b;
        return period;
    }

    public static Date str2MonthYear(String period, Integer date) throws BusinessException {
        Date result = null;
        if (StringUtils.isBlank(period)) {
            return result;
        }
        try {
            DateFormat df;
            if (period.matches("\\d{2}-\\d{4}")) {
                period = String.valueOf(date) + '-' + period;
                df = new SimpleDateFormat("dd-MM-yyyy");
            } else if (period.matches("\\d{2}/\\d{4}")) {
                period = String.valueOf(date) + '/' + period;
                df = new SimpleDateFormat(FORMAT_DDMMYYYY);
            } else if (period.matches("\\d{4}-\\d{2}")) {
                period = period + '-' + String.valueOf(date);
                df = new SimpleDateFormat(F_YYYYMMDD);
            } else if (period.matches("\\d{4}/\\d{2}")) {
                period = period + '/' + String.valueOf(date);
                df = new SimpleDateFormat("yyyy/MM/dd");
            } else {
                throw new BusinessException(ErrorEnum.INVALID_FORMAT_DATE);
            }
            result = df.parse(period);
        } catch (ParseException e) {
            throw new BusinessException(ErrorEnum.INVALID_FORMAT_DATE);
        }

        return result;
    }

    public static Date str2DateFormat(String dateStr, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateStr);
    }

    public static Calendar calendarFor(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal;
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static Date plusDate(Date startDate, int date) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, date);
        return cal.getTime();
    }

    /**
     * @param date
     * @return Lay ngay cuoi cung cua thang truoc
     */
    public static Calendar getLastDayPreviousMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int day = 1;
        Calendar lastMonthCal = calendarFor(year, month, day);
        lastMonthCal.add(Calendar.DATE, -1);
        return lastMonthCal;
    }

    public static String getMonthVn(int month) {
        return month < 10 ? "0" + month : "" + month;
    }

    public static String calendarToString(Calendar cal) {
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(F_YYYYMMDD);
        return format1.format(date);
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Integer subtractDate(Date date1, Date date2) {
        LocalDate localDate1 = convertToLocalDate(date1);
        LocalDate localDate2 = convertToLocalDate(date2);
        Period period = Period.between(localDate2, localDate1);
        return period.getDays();
    }

    /**
     * @param dt1
     * @param dt2
     * @return Return gia tri so ngay date 1 - date 2
     */
    public static Integer subtract(Date dt1, Date dt2) {
        long diff = Math.abs(dt1.getTime() - dt2.getTime());
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return Integer.valueOf(String.valueOf(diffDays));
    }

    public static Long minusDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }
        return TimeUnit.DAYS.convert(date1.getTime() - date2.getTime(), TimeUnit.MILLISECONDS);
    }

    public static XMLGregorianCalendar getXMLGregorianCalendar(String date, String format)
            throws ParseException, DatatypeConfigurationException {
        DateFormat dformat = new SimpleDateFormat(format);
        Date dDate = dformat.parse(date);

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dDate);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }

    public static Date convertStringToDate(String strDate, String pattern) {
        if (CommonUtil.isNullOrEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate currentDate = convertToLocalDate(new Date());
        LocalDate birthday = convertToLocalDate(birthDate);
        return Period.between(birthday, currentDate).getYears();
    }

    public static Date numToDate(Long numDate) throws BusinessException {
        try {
            if (numDate == null) {
                return null;
            }
            DateFormat inputDF = new SimpleDateFormat(FORMAT_YYYYMMDD);
            return inputDF.parse("" + numDate);
        } catch (ParseException e) {
            throw new BusinessException(ErrorEnum.INVALID_FORMAT_DATE);
        }
    }

    public static boolean isValidFormat(String input, String format) {
        if (CommonUtil.isNullOrEmpty(input)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
