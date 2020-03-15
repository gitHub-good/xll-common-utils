package com.xll.common.utils.base;

import com.xll.common.utils.enums.Consents;
import com.xll.common.utils.enums.DateFormatEnum;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @Author：xuliangliang
 * @Description：时间转换
 * @Date：1:15 上午 2020/3/16
 */
public class DateConvertUtils {

    /**
     * 字符串转时间
     * @param dateString 字符串时间
     * @param dateFormat 转换格式
     * @return
     */
    public static Date parse(String dateString, String dateFormat) {
        return parse(dateString, dateFormat, Date.class);
    }

    /**
     * 时间转换
     * @param dateString
     * @param dateFormat
     * @param targetResultType
     * @param <T>
     * @return
     */
    public static <T extends Date> T parse(String dateString, String dateFormat, Class<T> targetResultType) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        } else {
            if (dateString.length() == 10 && dateString.indexOf(Consents.MARK_ONE) > -1) {
                dateFormat = DateFormatEnum.FORMAT_DATE_ONE.getDateFormat();
            } else if (dateString.length() == 10 && dateString.indexOf(Consents.MARK_TWO) > -1) {
                dateFormat = DateFormatEnum.FORMAT_DATE_TWO.getDateFormat();
            }

            SimpleDateFormat df = new SimpleDateFormat(dateFormat);

            try {
                long time = df.parse(dateString).getTime();
                Date t = targetResultType.getConstructor(Long.TYPE).newInstance(time);
                return (T) t;
            } catch (ParseException var7) {
                String errorInfo = "cannot use dateFormat:" + dateFormat + " parse dateString:" + dateString;
                throw new IllegalArgumentException(errorInfo, var7);
            } catch (Exception var8) {
                throw new IllegalArgumentException("error targetResultType:" + targetResultType.getName(), var8);
            }
        }
    }

    /**
     * 日期转字符串
     * @param date
     * @param dateFormat
     * @return
     */
    public static String format(Date date, String dateFormat) {
        if (date == null) {
            return null;
        } else {
            DateFormat df = new SimpleDateFormat(dateFormat);
            return df.format(date);
        }
    }
}
