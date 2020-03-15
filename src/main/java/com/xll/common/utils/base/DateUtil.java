package com.xll.common.utils.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    private static Map<String, DateFormat> dateFormatCache = new HashMap();
    private static String defaultPattern;

    public static void applyPattern(String pattern) {
        defaultPattern = pattern;
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static Date today() {
        return roundDate(now());
    }

    public static String format(Date date) {
        if (defaultPattern == null) {
            throw new RuntimeException("Default Pattern is not initiated.");
        } else {
            return format(date, defaultPattern);
        }
    }

    public static String format(Date date, String pattern) {
        DateFormat df = (DateFormat)dateFormatCache.get(pattern);
        if (df == null) {
            df = new SimpleDateFormat(pattern);
            dateFormatCache.put(pattern, df);
        }

        return ((DateFormat)df).format(date);
    }

    public static Date parse(String str) throws ParseException {
        if (defaultPattern == null) {
            throw new RuntimeException("Default Pattern is not initiated.");
        } else {
            return parse(str, defaultPattern);
        }
    }

    public static Date parse(String str, String pattern) throws ParseException {
        DateFormat df = (DateFormat)dateFormatCache.get(pattern);
        if (df == null) {
            df = new SimpleDateFormat(pattern);
            dateFormatCache.put(pattern, df);
        }

        return ((DateFormat)df).parse(str);
    }

    public static Date addYear(Date date, int num) {
        return addTimeIntervals(date, 1, num);
    }

    public static Date addMonths(Date date, int num) {
        return addTimeIntervals(date, 2, num);
    }

    public static Date addDays(Date date, int num) {
        return addTimeIntervals(date, 5, num);
    }

    public static Date addHours(Date date, int num) {
        return addTimeIntervals(date, 10, num);
    }

    public static Date addMinutes(Date date, int num) {
        return addTimeIntervals(date, 12, num);
    }

    public static Date addSeconds(Date date, int num) {
        return addTimeIntervals(date, 13, num);
    }

    private static Date addTimeIntervals(Date date, int type, int num) {
        if (date == null) {
            throw new IllegalArgumentException();
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(type, num);
            return c.getTime();
        }
    }

    public static Calendar roundCalendar(Calendar _c) {
        _c.set(11, 0);
        _c.set(12, 0);
        _c.set(13, 0);
        _c.set(14, 0);
        return _c;
    }

    public static Date roundDate(Date _d) {
        Calendar c = Calendar.getInstance();
        c.setTime(_d);
        return roundCalendar(c).getTime();
    }

    public static java.sql.Date toSQLDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static Date toUtilDate(java.sql.Date date) {
        return new Date(date.getTime());
    }

    public static int getInterval(Date _one, Date _two) {
        Calendar one = Calendar.getInstance();
        one.setTime(_one);
        Calendar two = Calendar.getInstance();
        two.setTime(_two);
        int yearOne = one.get(1);
        int yearTwo = two.get(1);
        int dayOne = one.get(6);
        int dayTwo = two.get(6);
        if (yearOne == yearTwo) {
            return dayOne - dayTwo;
        } else {
            int yearDays;
            if (yearOne < yearTwo) {
                for(yearDays = 0; yearOne < yearTwo; ++yearOne) {
                    if (isLeapyear(yearOne)) {
                        yearDays += 366;
                    } else {
                        yearDays += 365;
                    }
                }

                return dayOne - yearDays - dayTwo;
            } else {
                for(yearDays = 0; yearTwo < yearOne; ++yearTwo) {
                    if (isLeapyear(yearTwo)) {
                        yearDays += 366;
                    } else {
                        yearDays += 365;
                    }
                }

                return dayOne - dayTwo + yearDays;
            }
        }
    }

    public static int getMonthInterval(Date _one, Date _two) {
        Calendar one = Calendar.getInstance();
        one.setTime(_one);
        Calendar two = Calendar.getInstance();
        two.setTime(_two);
        int yearInt = two.get(1) - one.get(1);
        int monthInt = two.get(2) - one.get(2);
        int dayInt = two.get(5) - one.get(5);
        return yearInt * 12 + monthInt + (dayInt > 0 ? 1 : 0);
    }

    public static boolean isLeapyear(Date _date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_date);
        int year = calendar.get(1);
        return isLeapyear(year);
    }

    public static boolean isLeapyear(int _year) {
        if (_year % 4 == 0) {
            if (_year % 100 == 0) {
                return _year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
