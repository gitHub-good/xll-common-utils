package com.xll.common.utils.base;

import java.util.List;

public class StringUtils {
    public static final String DELIM = ",";
    private static final char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String join(List<? extends Object> list) {
        return join(list, ",");
    }

    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    public static String join(List<? extends Object> list, String seperator) {
        if (list != null && list.size() != 0) {
            Object[] t = new Object[0];
            return join(list.toArray(t), seperator);
        } else {
            return "";
        }
    }

    public static String join(Object[] arr, String seperator) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < arr.length; ++i) {
            sb.append(seperator + arr[i]);
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }

    public static String[] split(String str) {
        return (String[])null;
    }

    public static String[] split(String str, String seperator) {
        return (String[])null;
    }

    public static String left(String str, int index) {
        return null;
    }

    public static String right(String str, int index) {
        return null;
    }

    public static String bytes2String(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            StringBuffer sb = new StringBuffer();
            byte[] var2 = bytes;
            int var3 = bytes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                sb.append(byte2HEX(b));
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static String byte2HEX(byte ib) {
        char[] ob = new char[]{Digit[ib >>> 4 & 15], Digit[ib & 15]};
        String s = new String(ob);
        return s;
    }
}
