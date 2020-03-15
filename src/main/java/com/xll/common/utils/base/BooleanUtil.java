package com.xll.common.utils.base;

/**
 *
 * @Author：xuliangliang
 * @Description：布尔工具类
 * @Date：1:03 上午 2020/3/16
 */
public class BooleanUtil {

    public static boolean toBoolean(Object str) {
        return str == null ? false : toBoolean(str.toString(), false);
    }

    /**
     * 判断字符串是否为true字符串
     * @param str
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(String str, boolean defaultValue) {
        if (StringUtil.empty(str)) {
            return defaultValue;
        } else {
            try {
                return toRawBoolean(str);
            } catch (Exception var3) {
                var3.printStackTrace();
                return defaultValue;
            }
        }
    }

    public static boolean toRawBoolean(String str) throws Exception {
        return "true".equalsIgnoreCase(str.trim());
    }
}
