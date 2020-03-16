package com.xll.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @Author：xuliangliang
 * @Description：数字工具类
 * @Date：12:44 下午 2020/3/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class NumberTools {
    private static final Logger logger = LoggerFactory.getLogger(NumberTools.class);

    public static Long toLong(final String str, final Long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str.trim());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static long toLong(final String str) {
        return toLong(str, 0L);
    }

    public static long toLong(final Object obj) {
        if (obj == null) {
            return 0;
        } else {
            return toLong(obj.toString());
        }
    }

    public static int toInt(final String str) {
        return toInt(str, 0);
    }

    public static int toInt(final String str, final int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Integer toInteger(final String str, final Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static Integer parseInt(String source) {
        return parseInt(source, null);
    }

    public static Integer parseInt(String source, Integer defaultValue) {
        if (StringUtils.isEmpty(source))
            return defaultValue;

        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    public static Short parseShort(String source) {
        return parseShort(source, null);
    }

    public static Short parseShort(String source, Short defaultValue) {
        if (StringUtils.isEmpty(source))
            return defaultValue;

        try {
            return Short.parseShort(source);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    public static Double parseDouble(String source) {
        return parseDouble(source, null);
    }

    public static Double parseDouble(String source, Double defaultValue) {
        if (StringUtils.isEmpty(source))
            return defaultValue;

        try {
            return Double.parseDouble(source);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 若某数为null，则强制转为 rst
     *
     * @param first
     * @param rst
     * @return
     */
    public static int nvl(Integer first, int rst) {
        return first == null ? rst : first;
    }

    public static int nvl(Integer first) {
        return nvl(first, 0);
    }

    public static float nvl(Float first, float rst) {
        return first == null ? rst : first;
    }

    public static float nvl(Float first) {
        return nvl(first, 0);
    }

    @SuppressWarnings("squid:S2140")
    public static int getRandInt(int rangeStart, int rangeEnd) {
        return rangeStart + (int) (Math.random() * (rangeEnd - rangeStart + 1));
    }

    /**
     * 功能描述: 支持将BigDecimal/Long/Integer/String转换成int<br>
     */
    public static int convertObj2Int(final Object obj) {
        if (obj == null)
            return 0;

        if (obj instanceof BigDecimal)
            return ((BigDecimal) obj).intValue();
        else if (obj instanceof Long)
            return ((Long) obj).intValue();
        else if (obj instanceof Integer)
            return ((Integer) obj).intValue();
        else if (obj instanceof String)
            return toInt((String) obj);
        else
            return 0;
    }

    /**
     * 功能描述: 判断一批long类型的数据，是否都大于0<br>
     */
    public static boolean isAllGreaterThanZero(long... ids) {
        if (ids == null) {
            return false;
        }

        for (long id : ids) {
            if (id <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证Long参数
     */
    public static boolean checkLongParams(Long... params) {
        if (params == null) {
            return false;
        }
        for (Long param : params) {
            if (param == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证Integer参数
     */
    public static boolean checkIntegerParams(Integer... params) {
        if (params == null) {
            return false;
        }
        for (Integer param : params) {
            if (param == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 四舍五入截取两位小数
     *
     * @param d 需要格式化的数据
     * @param len 小数点后位数， 默认为2
     * @return
     */
    public static String roundFormat(double d, Integer... len) {
        int lenth = 2;
        if (len != null && len.length > 0) {
            lenth = len[0];
            if (lenth > 10 || lenth < 0) {
                lenth = 2;
            }
        }
        String formater = "#0.0000000000".substring(0, 3 + lenth);
        DecimalFormat df = new DecimalFormat(formater);
        return df.format(d);
    }

    /**
     * 四舍五入截取两位小数
     *
     * @param d 需要格式化的数据
     * @param addZero 小数位数不足是否补零
     * @param len 小数点后位数， 默认为2
     * @return
     */
    public static String roundFormat(double d, boolean addZero, Integer... len) {
        if (addZero) { // 小数不足补零
            return roundFormat(d, len);
        } else {
            BigDecimal bigDeci = BigDecimal.valueOf(d);
            int lenth = 2;
            if (len != null && len.length > 0) {
                lenth = len[0];
                if (lenth > 10 || lenth < 0) {
                    lenth = 2;
                }
            }
            if (bigDeci.scale() < lenth) {
                return String.valueOf(d);
            }
            return roundFormat(d, len);
        }
    }

    /**
     * 功能描述: 获取包装器类型的真实值<br>
     *
     * @param i
     * @param defaultValue
     * @return
     */
    public static int integerValue(Integer i, int defaultValue) {
        return i == null ? defaultValue : i.intValue();
    }

}
