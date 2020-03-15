package com.xll.common.utils.sftp;

import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtil {
    public static final String PATTERN_MONITOR_TIME = "yy-MM-dd HH";
    public static final String PATTERN_FULL_HOUR = "yyyy-MM-dd HH";
    public static final String HOUR = "HH";
    public static final String PATTERN_SIMPLE = "yyyy-MM-dd";
    public static final String PATTERN_MINUTES = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_NORMAL = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss S";
    public static final String PATTERN_FULL_SIMPLE = "yyyyMMddHHmmss";
    public static final String PATTERN_FULL_DAY = "yyyyMMdd";
    public static final String ORACLE_FORMAT = "YYYY-MM-DD HH24:MI:SS";
    public static final String SSSS = "yyyyMMddHHmmssSSSS";
    private static final ThreadLocal<DatatypeFactory> threadDTF = new ThreadLocal();

    public DateUtil() {
    }

    public static Date parse(String src, String pattern) throws ParseException {
        SimpleDateFormat util = new SimpleDateFormat();
        util.applyPattern(pattern);
        return util.parse(src);
    }

    public static Date parse(String src) {
        SimpleDateFormat util = new SimpleDateFormat("yyyy-MM-dd");
        Date ret = null;
        if (!StringUtils.isEmpty(src)) {
            util.applyPattern("yyyy-MM-dd HH:mm:ss");

            try {
                ret = util.parse(src);
            } catch (ParseException var6) {
            }

            if (ret == null) {
                try {
                    util.applyPattern("yyyy-MM-dd");
                    ret = util.parse(src);
                } catch (ParseException var5) {
                }
            }

            if (ret == null) {
                try {
                    util.applyPattern("yyyy-MM-dd HH:mm:ss S");
                    ret = util.parse(src);
                } catch (ParseException var4) {
                }
            }
        }

        if (ret == null) {
            throw new IllegalArgumentException("## cant parse to Date . not supported by default pattern: $" + src + "$");
        } else {
            return ret;
        }
    }

    public static Date parse(Long timeMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        calendar.add(11, 8);
        return calendar.getTime();
    }

    public static Date getDayBegin(Date date) {
        if (date == null) {
            date = nowDate();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.clear(11);
        cal.clear(12);
        cal.clear(13);
        cal.clear(14);
        return cal.getTime();
    }

    public static Date getDayEnd(Date date) {
        Date ret = getDayBegin(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ret);
        calendar.add(6, 1);
        calendar.add(13, -1);
        return calendar.getTime();
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat util = new SimpleDateFormat(pattern);
        String str = util.format(date);
        return str;
    }

    public static Date addField(Date date, int field, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, num);
        return cal.getTime();
    }

    public static Date addMinutes(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(12, minutes);
        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(13, seconds);
        return calendar.getTime();
    }

    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(10, hours);
        return calendar.getTime();
    }

    public static Date clearMinutesAndSeconds(Date date) {
        if (null == date) {
            throw new IllegalArgumentException();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.clear(12);
            calendar.clear(13);
            calendar.clear(14);
            return calendar.getTime();
        }
    }

    public static Date clearSeconds(Date date) {
        if (null == date) {
            throw new IllegalArgumentException();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.clear(13);
            calendar.clear(14);
            return calendar.getTime();
        }
    }

    public static String now() {
        return formatDate(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
    }

    public static String now(String dateFormat) {
        return formatDate(Calendar.getInstance().getTime(), dateFormat);
    }

    public static Date nowDate() {
        return Calendar.getInstance().getTime();
    }

    public static String day() {
        return formatDate(Calendar.getInstance().getTime(), "yyyyMMdd");
    }

    public static String ssss() {
        return formatDate(Calendar.getInstance().getTime(), "yyyyMMddHHmmssSSSS");
    }

    public static String compare(Date beginTime, Date endTime) {
        long l = endTime.getTime() - beginTime.getTime();
        long day = l / 86400000L;
        long hour = l / 3600000L - day * 24L;
        long min = l / 60000L - day * 24L * 60L - hour * 60L;
        long s = l / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    public static DatatypeFactory getDTF() {
        DatatypeFactory dtf = (DatatypeFactory)threadDTF.get();
        if (dtf == null) {
            try {
                dtf = DatatypeFactory.newInstance();
            } catch (Exception var2) {
                var2.printStackTrace();
            }

            threadDTF.set(dtf);
        }

        return dtf;
    }

    public static XMLGregorianCalendar geneXMLGregorianCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getDTF().newXMLGregorianCalendar(cal.get(1), cal.get(2), cal.get(5), cal.get(11), cal.get(12), cal.get(13), 0, cal.get(15));
    }

    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;

        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return gc;
    }

    public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }
}
