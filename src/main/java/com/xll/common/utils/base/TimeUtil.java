package com.xll.common.utils.base;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtil extends DateFormatUtils {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final String theTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String otherTimeFormat = "yyyyMMddHHmmss";
    public static final String yearMonthTimeFormat = "yyyyMM";
    public static final String yearMonthDayTimeFormat = "yyyyMMdd";
    public static final String yearMonthDayFormat = "yyyy-MM-dd";
    public static final boolean useFastDateFormatter = true;
    private static final SimpleDateFormat theTimeFormator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat nameDf = new SimpleDateFormat("ddHHmmss");
    public static final DateFormat pathDf = new SimpleDateFormat("yyyyMM");
    private static Map<String, Object> theFastTimeFormatterMap = new HashMap();

    private TimeUtil() {
    }

    public static void main(String[] args) {
        System.out.println("获取本周一日期:" + toString(getMondayOFWeek()));
        System.out.println("获取本周日的日期~:" + toString(getCurrentWeekday()));
        System.out.println("获取上周一日期:" + toString(getPreviousWeekday()));
        System.out.println("获取上周日日期:" + toString(getPreviousWeekSunday()));
        System.out.println("获取下周一日期:" + toString(getNextMonday()));
        System.out.println("获取下周日日期:" + toString(getNextSunday()));
        System.out.println("获取本月第一天日期:" + toString(getFirstDayOfMonth()));
        System.out.println("获取本月最后一天日期:" + toString(getDefaultDay()));
        System.out.println("获取上月第一天日期:" + toString(getPreviousMonthFirst()));
        System.out.println("获取上月最后一天的日期:" + toString(getPreviousMonthEnd()));
        System.out.println("获取下月第一天日期:" + toString(getNextMonthFirst()));
        System.out.println("获取下月最后一天日期:" + toString(getNextMonthEnd()));
        System.out.println("获取本年的第一天日期:" + toString(getCurrentYearFirst()));
        System.out.println("获取本年最后一天日期:" + toString(getCurrentYearEnd()));
        System.out.println("获取去年的第一天日期:" + toString(getPreviousYearFirst()));
        System.out.println("获取去年的最后一天日期:" + toString(getPreviousYearEnd()));
        System.out.println("获取明年第一天日期:" + toString(getNextYearFirst()));
        System.out.println("获取明年最后一天日期:" + toString(getNextYearEnd()));
        System.out.println("获取本季度第一天到最后一天:" + toString(getThisSeasonTime(11)[0]) + "," + toString(getThisSeasonTime(11)[1]));
        System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9.29:" + getTwoDay("2008-12-1 12:30:00", "2008-9-29"));
    }

    public static final DateFormat getTimeFormatter(String format) {
        DateFormat sdf = (DateFormat)theFastTimeFormatterMap.get(format);
        return (DateFormat)(sdf != null ? sdf : new SimpleDateFormat(format));
    }

    public static SimpleDateFormat newTimeFormatter(String format) {
        return new SimpleDateFormat(format);
    }

    public static boolean equals(Calendar c1, Calendar c2) {
        if (c1 == c2) {
            return true;
        } else if (null != c1 && null != c2) {
            long t1 = c1.getTime().getTime() / 1000L;
            long t2 = c2.getTime().getTime() / 1000L;
            return t1 == t2;
        } else {
            return false;
        }
    }

    public static Calendar clone(Calendar cal) {
        Calendar c = Calendar.getInstance();
        c.set(cal.get(1), cal.get(2), cal.get(5), cal.get(11), cal.get(12), cal.get(13));
        c.set(14, cal.get(14));
        return c;
    }

    public static Date getSysTimestamp() {
        return Calendar.getInstance().getTime();
    }

    public static String toString(Calendar cal, DateFormat formator) {
        return null == cal ? "" : formator.format(cal.getTime());
    }

    public static String toString(Calendar cal, String format) {
        return null == cal ? "" : toString(cal, getTimeFormatter(format));
    }

    public static String toString(Date date, DateFormat formator) {
        return null == date ? "" : formator.format(date);
    }

    public static String toString(Date date, String format) {
        if (null == date) {
            return "";
        } else {
            if (StringUtil.empty(format)) {
                format = "yyyy-MM-dd";
            }

            return toString(date, getTimeFormatter(format));
        }
    }

    public static String toString(String format) {
        return toString(Calendar.getInstance(), format);
    }

    public static String toString(String time, String fromFormat, String toFormat) {
        try {
            return toString(toCalendar(time, fromFormat), toFormat);
        } catch (Exception var4) {
            return time;
        }
    }

    public static String toString(Calendar cal) {
        return toString((Calendar)cal, (DateFormat)theTimeFormator);
    }

    public static String toString(Date date) {
        return toString((Date)date, (DateFormat)theTimeFormator);
    }

    public static String toString(long millSeconds) {
        Date date = new Date(millSeconds);
        return toString(date);
    }

    public static String toString(long millSeconds, String format) {
        Date date = new Date(millSeconds);
        return toString(date, format);
    }

    public static long toNumber(Calendar cal, String format) {
        String time = toString(cal, format);
        return Long.parseLong(time);
    }

    public static long toNumber(String strTime, String timeFormat, String numberFormat) {
        try {
            return toNumber(toCalendar(strTime, timeFormat), numberFormat);
        } catch (Exception var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    public static Long toLong(Calendar time) {
        try {
            return new Long(time.getTime().getTime());
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Long toLong(Date time) {
        try {
            return new Long(time.getTime());
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static long toLong(Date time, String format) {
        String str = toString(time, format);
        return NumberUtil.toLong(str);
    }

    public static long toNumberOfyyyyMMdd(Calendar cal) {
        return (long)((cal.get(1) * 100 + cal.get(2) + 1) * 100 + cal.get(5));
    }

    public static Calendar toRawCalendar(String time, DateFormat formator) throws Exception {
        Calendar cal = Calendar.getInstance();
        Date d = formator.parse(time);
        cal.setTime(d);
        cal.set(14, 0);
        return cal;
    }

    public static Calendar toCalendar(String time, DateFormat formator) {
        try {
            return toRawCalendar(time, formator);
        } catch (Exception var3) {
            var3.printStackTrace();
            return Calendar.getInstance();
        }
    }

    public static Calendar toRawCalendar(String time, String format) throws Exception {
        return toRawCalendar(time, getTimeFormatter(format));
    }

    public static Calendar toCalendar(String time, String format) throws Exception {
        return toRawCalendar(time, getTimeFormatter(format));
    }

    public static Calendar toRawCalendar(long time, String format) throws Exception {
        return toRawCalendar(String.valueOf(time), getTimeFormatter(format));
    }

    public static Calendar toCalendar(long time, String format) {
        return toCalendar(String.valueOf(time), getTimeFormatter(format));
    }

    public static Calendar toRawCalendar(String time) throws Exception {
        return toRawCalendar(time, (DateFormat)theTimeFormator);
    }

    public static Calendar toCalendar(String date) {
        String parse = date.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");
        parse = parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");
        return toCalendar(date, (DateFormat)(new SimpleDateFormat(parse)));
    }

    public static Calendar toBoundaryCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, NumberUtil.adjustRange(year, 2000, 2010));
        cal.set(2, NumberUtil.adjustRange(month, 1, 12) - 1);
        cal.set(5, NumberUtil.adjustRange(day, 1, cal.getActualMaximum(5)));
        cal.set(11, NumberUtil.adjustRange(hour, 0, 23));
        cal.set(12, NumberUtil.adjustRange(minute, 0, 59));
        cal.set(13, NumberUtil.adjustRange(second, 0, 59));
        cal.set(14, 0);
        return cal;
    }

    public static Date toRawStartRegularBoundaryDate(String time, String delim) throws Exception {
        return toRawRegularBoundaryDate(time, delim, 2000, 1, 1, 0, 0, 0);
    }

    public static Date toRawEndRegularBoundaryDate(String time, String delim) throws Exception {
        try {
            return toRawRegularBoundaryDate(time, delim, 2010, 12, 31, 23, 59, 59);
        } catch (Exception var3) {
            throw new Exception("toRawRegularStartDate, failure, exception = " + var3);
        }
    }

    public static Date toRawRegularBoundaryDate(String time, String delim, int year, int month, int day, int hour, int minute, int second) throws Exception {
        String[] set = StringUtil.split(time, delim);
        if (set.length <= 0) {
            throw new Exception("time format illegal, time = " + time + ", delim = " + delim);
        } else {
            if (set.length >= 1) {
                year = NumberUtil.toRawInt(set[0]);
            }

            if (set.length >= 2) {
                month = NumberUtil.toRawInt(set[1]);
            }

            if (set.length >= 3) {
                day = NumberUtil.toRawInt(set[2]);
            }

            Calendar cal = toBoundaryCalendar(year, month, day, hour, minute, second);
            return cal.getTime();
        }
    }

    public static Calendar minTime(Calendar time1, Calendar time2) {
        return null != time1 && !time1.after(time2) ? time1 : time2;
    }

    public static Date minTime(Date time1, Date time2) {
        if (null == time1 && null == time2) {
            return null;
        } else if (null == time1 && null != time2) {
            return time2;
        } else if (null != time1 && null == time2) {
            return time1;
        } else {
            return time1.after(time2) ? time2 : time1;
        }
    }

    public static Calendar maxTime(Calendar time1, Calendar time2) {
        return null != time1 && !time1.before(time2) ? time1 : time2;
    }

    public static Date maxTime(Date time1, Date time2) {
        if (null == time1 && null == time2) {
            return null;
        } else if (null == time1 && null != time2) {
            return time2;
        } else if (null != time1 && null == time2) {
            return time1;
        } else {
            return time1.before(time2) ? time2 : time1;
        }
    }

    public static boolean notAfter(Calendar time1, Calendar time2) {
        if (null != time1 && null == time2) {
            return false;
        } else if (null == time1 && null != time2) {
            return false;
        } else {
            return null == time1 || null == time2 || !time1.after(time2);
        }
    }

    public static boolean notAfter(Date time1, Date time2) {
        if (null != time1 && null == time2) {
            return false;
        } else if (null == time1 && null != time2) {
            return false;
        } else {
            return null == time1 || null == time2 || !time1.after(time2);
        }
    }

    public static boolean equalOrBetween(Calendar time, Calendar start, Calendar end) {
        return notAfter(start, time) && notAfter(time, end);
    }

    public static boolean equalOrBetween(Date time, Date start, Date end) {
        return notAfter(start, time) && notAfter(time, end);
    }

    public static String getTwoTimeMinute(String stratTime, String endTime) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        long minute = 0L;
        long second = 0L;

        try {
            Date date = myFormatter.parse(stratTime);
            Date mydate = myFormatter.parse(endTime);
            minute = (date.getTime() - mydate.getTime()) / 60000L;
            second = (date.getTime() - mydate.getTime()) / 1000L - minute * 60L;
        } catch (Exception var9) {
            return "";
        }

        return minute + "分钟" + second + "秒";
    }

    public static Calendar getRandomCalendar() {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 0, 1);
        long start = cal.getTimeInMillis();
        cal.set(2008, 0, 1);
        long end = cal.getTimeInMillis();
        Date d = new Date(start + (long)(rand.nextDouble() * (double)(end - start)));
        cal.setTime(d);
        return cal;
    }

    public static Double getTwoDay(String sj1, String sj2) {
        Double day = 0.0D;

        try {
            Long times = toCalendar(sj1).getTimeInMillis() - toCalendar(sj2).getTimeInMillis();
            day = (double)times / 8.64E7D;
            return day;
        } catch (Exception var4) {
            return 0.0D;
        }
    }

    public static Integer getSecondBetweenDate(Date d1, Date d2) {
        Long second = (d2.getTime() - d1.getTime()) / 1000L;
        return second.intValue();
    }

    public static int getDaysBetweenDate(Date begin, Date end) {
        return (int)((end.getTime() - begin.getTime()) / 86400000L);
    }

    public static String getWeek(String sdate) {
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (new SimpleDateFormat("EEEE")).format(c.getTime());
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static long getDays(String date1, String date2) {
        if (date1 != null && !date1.equals("")) {
            if (date2 != null && !date2.equals("")) {
                SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Date mydate = null;

                try {
                    date = myFormatter.parse(date1);
                    mydate = myFormatter.parse(date2);
                } catch (Exception var7) {
                }

                long day = (date.getTime() - mydate.getTime()) / 86400000L;
                return day;
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public static Date getDefaultDay() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(5, 1);
        lastDate.add(2, 1);
        lastDate.add(5, -1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getPreviousMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(5, 1);
        lastDate.add(2, -1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getFirstDayOfMonth() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(5, 1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getCurrentWeekday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 6);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(7) - 1;
        return dayOfWeek == 1 ? 0 : 1 - dayOfWeek;
    }

    public static Date getMondayOFWeek() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date getPreviousWeekSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus - 1);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date getPreviousWeekday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + -7);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date getNextMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date getNextSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, mondayPlus + 7 + 6);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date getPreviousMonthEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(2, -1);
        lastDate.set(5, 1);
        lastDate.roll(5, -1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getNextMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(2, 1);
        lastDate.set(5, 1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getNextMonthEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(2, 1);
        lastDate.set(5, 1);
        lastDate.roll(5, -1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getNextYearEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(1, 1);
        lastDate.set(6, 1);
        lastDate.roll(6, -1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static Date getNextYearFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(1, 1);
        lastDate.set(6, 1);
        lastDate.set(11, 0);
        lastDate.set(12, 0);
        lastDate.set(13, 0);
        return lastDate.getTime();
    }

    public static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(6, 1);
        cd.roll(6, -1);
        int MaxYear = cd.get(6);
        return MaxYear;
    }

    private static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(6);
        cd.set(6, 1);
        cd.roll(6, -1);
        int MaxYear = cd.get(6);
        return yearOfNumber == 1 ? -MaxYear : 1 - yearOfNumber;
    }

    public static Date getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, yearPlus);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date getCurrentYearEnd() {
        String str = toString(Calendar.getInstance(), "yyyy") + "-12-31";
        Calendar currentDate = toCalendar(str);
        return currentDate.getTime();
    }

    public static Date getPreviousYearFirst() {
        String years = toString(Calendar.getInstance(), "yyyy");
        int years_value = NumberUtil.toInt(years);
        --years_value;
        return toCalendar(years_value + "-01-01").getTime();
    }

    public static Date getPreviousYearEnd() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(5, yearPlus + 0 + -1);
        getThisSeasonTime(11);
        currentDate.set(11, 0);
        currentDate.set(12, 0);
        currentDate.set(13, 0);
        return currentDate.getTime();
    }

    public static Date[] getThisSeasonTime(int month) {
        int[][] array = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }

        if (month >= 4 && month <= 6) {
            season = 2;
        }

        if (month >= 7 && month <= 9) {
            season = 3;
        }

        if (month >= 10 && month <= 12) {
            season = 4;
        }

        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];
        String years = toString(Calendar.getInstance(), "yyyy");
        int years_value = NumberUtil.toInt(years);
        int start_days = 1;
        int end_days = getLastDayOfMonth(years_value, end_month);
        return new Date[]{toCalendar(years_value + "-" + start_month + "-" + start_days).getTime(), toCalendar(years_value + "-" + end_month + "-" + end_days).getTime()};
    }

    private static int getLastDayOfMonth(int year, int month) {
        if (month != 1 && month != 3 && month != 5 && month != 7 && month != 8 && month != 10 && month != 12) {
            if (month != 4 && month != 6 && month != 9 && month != 11) {
                if (month == 2) {
                    return isLeapYear(year) ? 29 : 28;
                } else {
                    return 0;
                }
            } else {
                return 30;
            }
        } else {
            return 31;
        }
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static boolean isInDate(Date date, Date compareDate) {
        return compareDate.after(getStartDate(date)) && compareDate.before(getFinallyDate(date));
    }

    public static Date getFinallyDate(Date date) {
        String temp = toString(date);
        temp = temp + " 23:59:59";
        return toCalendar(temp).getTime();
    }

    public static Date getStartDate(Date date) {
        String temp = toString(date);
        temp = temp + " 00:00:00";
        return toCalendar(temp).getTime();
    }

    public static String formatDate(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public static String formatTime(Date date) {
        return DateFormat.getTimeInstance().format(date);
    }

    public static String formatDateTime(Date date) {
        return DateFormat.getDateTimeInstance().format(date).contains("0:00:00") ? formatDate(date) : DateFormat.getDateTimeInstance().format(date);
    }

    public static Date parseTime(Date time) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date result = time;

        try {
            result = format.parse(formatTime(time));
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static Date parseDate(Date time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date result = time;

        try {
            result = format.parse(formatDate(time));
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static Date getSpecficDateStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(6, amount);
        return getStartDate(cal.getTime());
    }

    public static Date getSpecficWeekStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(2);
        cal.add(4, amount);
        cal.set(7, 2);
        return getStartDate(cal.getTime());
    }

    public static Date getSpecficMonthStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(2, amount);
        cal.set(5, 1);
        return getStartDate(cal.getTime());
    }

    public static Date getSpecficMonthEnd(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSpecficMonthStart(date, amount + 1));
        cal.add(6, -1);
        return getFinallyDate(cal.getTime());
    }

    public static Date getSpecficYearStart(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(1, amount);
        cal.set(6, 1);
        return getStartDate(cal.getTime());
    }

    public static Date getSpecficYearEnd(Date date, int amount) {
        Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        cal.add(6, -1);
        return getFinallyDate(cal.getTime());
    }
}
