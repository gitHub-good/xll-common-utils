package com.xll.common.utils.base;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
    private static final NumberFormat theNumberFormatter = NumberFormat.getNumberInstance();

    public NumberUtil() {
    }

    public static boolean isLong(String str) {
        try {
            long l = Long.parseLong(str);
            return true;
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static String toString(double num, int fractionDigit) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(1);
        nf.setMinimumFractionDigits(fractionDigit);
        nf.setMaximumFractionDigits(fractionDigit);
        return nf.format(num);
    }

    public static String toString(double num) {
        NumberFormat f = NumberFormat.getInstance();
        if (f instanceof DecimalFormat) {
            ((DecimalFormat)f).setDecimalSeparatorAlwaysShown(true);
        }

        f.setParseIntegerOnly(true);
        return f.format(num);
    }

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static int toInt(Object str) {
        return toInt(str + "", 0);
    }

    public static Integer toIntObject(String str) {
        return new Integer(str);
    }

    public static int toInt(String str, int defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return !isInteger(str) ? defaultValue : toRawInt(str);
            } catch (Exception var3) {
                var3.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static int toRawInt(String str) throws Exception {
        return Integer.parseInt(str.trim());
    }

    public static int toFormattedInt(String str) {
        return toFormattedInt(str, 0);
    }

    public static int toFormattedInt(String str, int defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawFormattedInt(str);
            } catch (Exception var3) {
                var3.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static int toRawFormattedInt(String str) throws Exception {
        return Integer.parseInt(theNumberFormatter.parse(str.trim()).toString());
    }

    public static long toLong(Object str) {
        return toLong(str + "");
    }

    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    public static Long toLongObject(String str) {
        return new Long(str.trim());
    }

    public static Long toLongObject(String str, long defaultValue) {
        return StringUtil.empty(str) ? new Long(defaultValue) : new Long(str.trim());
    }

    public static Long toLong(Object object, long defaultValue) {
        String str = object == null ? "" : object + "";
        return toLong(str, defaultValue);
    }

    public static long toLong(String str, long defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawLong(str);
            } catch (Exception var4) {
                var4.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static long toRawLong(String str) throws Exception {
        return Long.parseLong(str.trim());
    }

    public static long toFormattedLong(String str) {
        return toFormattedLong(str, 0L);
    }

    public static long toFormattedLong(String str, long defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawFormattedLong(str);
            } catch (Exception var4) {
                var4.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static long toRawFormattedLong(String str) throws Exception {
        return Long.parseLong(theNumberFormatter.parse(str.trim()).toString());
    }

    public static double toDouble(Object str) {
        return toDouble((String)str, 0.0D);
    }

    public static double toDouble(double num, int fractionDigit) {
        String d = toString(num, fractionDigit);
        return toDouble(d, 0.0D);
    }

    public static double toDouble(String str, double defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawDouble(str);
            } catch (Exception var4) {
                var4.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static double toDouble(double value, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return new Double(df.format(value));
    }

    public static double toRawDouble(String str) throws Exception {
        return Double.parseDouble(str.trim());
    }

    public static double toFormattedDouble(String str) {
        return toFormattedDouble(str, 0.0D);
    }

    public static double toFormattedDouble(String str, double defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawFormattedDouble(str);
            } catch (Exception var4) {
                var4.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static double toRawFormattedDouble(String str) throws Exception {
        return Double.parseDouble(theNumberFormatter.parse(str.trim()).toString());
    }

    public static float toFloat(Object str) {
        return toFloat(str + "", 0.0F);
    }

    public static float toFloat(String str, float defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawFloat(str);
            } catch (Exception var3) {
                var3.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static float toRawFloat(String str) throws Exception {
        return Float.parseFloat(str.trim());
    }

    public static Integer[] toIntegerObjectSet(int[] set) {
        if (set == null) {
            return null;
        } else {
            Integer[] s = new Integer[set.length];

            for(int i = 0; i < set.length; ++i) {
                s[i] = new Integer(set[i]);
            }

            return s;
        }
    }

    public static int[] splitToIntSet(String str, String delim) {
        int[] intSet = new int[0];
        String[] strSet = StringUtil.split(str, delim);
        if (null == strSet) {
            return intSet;
        } else {
            for(int i = 0; i < strSet.length; ++i) {
                try {
                    intSet = ArrayUtil.addIntArray(intSet, toRawInt(strSet[i]));
                } catch (Exception var6) {
                }
            }

            return intSet;
        }
    }

    public static String join(int[] set, String delim) {
        if (null != set && set.length > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(set[0]);

            for(int i = 1; i < set.length; ++i) {
                sb.append(delim);
                sb.append(set[i]);
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static final int adjustRange(int old, int min, int max) {
        if (old < min) {
            old = min;
        }

        if (old > max) {
            old = max;
        }

        return old;
    }

    public static final int adjustMinRange(int old, int min) {
        if (old < min) {
            old = min;
        }

        return old;
    }

    public static final int adjustMaxRange(int old, int max) {
        if (old > max) {
            old = max;
        }

        return old;
    }

    public static final String toString(long[] array) {
        if (null == array) {
            return "count=0:[]";
        } else {
            String s = "count=" + array.length + ":[ ";

            for(int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    s = s + ", ";
                }

                s = s + array[i];
            }

            s = s + " ]";
            return s;
        }
    }

    public static final String toString(int[] array) {
        if (null == array) {
            return "count=0:[]";
        } else {
            String s = "count=" + array.length + ":[ ";

            for(int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    s = s + ", ";
                }

                s = s + array[i];
            }

            s = s + " ]";
            return s;
        }
    }

    public static final String parseNumber(int num) {
        String[] digit = new String[]{"0", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = new String[]{"#", "十", "百", "千", "万", "十", "百", "千", "亿"};
        StringBuffer sb = new StringBuffer();

        for(int unitIndex = 0; num > 0; num /= 10) {
            int t = num % 10;
            if (t > 0 || unitIndex % 4 == 0) {
                sb.append(unit[unitIndex]);
            }

            if (unitIndex != 0 || t != 0) {
                sb.append(digit[t]);
            }

            ++unitIndex;
        }

        return sb.reverse().toString().replaceAll("0{2,}", "0").replaceAll("0万", "万").replaceFirst("0#", "").replaceFirst("#", "");
    }
}
