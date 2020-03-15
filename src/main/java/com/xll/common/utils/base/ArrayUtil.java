package com.xll.common.utils.base;

import org.apache.poi.ss.formula.functions.T;

import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @Author：xuliangliang
 * @Description：数组工具类
 * @Date：3:59 下午 2020/3/15
 */
public class ArrayUtil {

    public static boolean empty(Object[] set) {
        return null == set || set.length <= 0;
    }

    public static boolean empty(Collection<?> set) {
        return null == set || set.size() <= 0;
    }

    public static boolean empty(int[] set) {
        return null == set || set.length <= 0;
    }

    /**
     * 数组中是否有该坐标的数据值
     * @param set
     * @param val
     * @return
     */
    public static boolean has(int[] set, int val) {
        if (empty(set)) {
            return false;
        } else {
            for(int i = 0; i < set.length; ++i) {
                if (set[i] == val) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * 查询数组最大的数据序号
     * @param set
     * @return
     */
    public static int max(int[] set) {
        if (empty(set)) {
            return 0;
        } else {
            int m = set[0];

            for(int i = 1; i < set.length; ++i) {
                m = Math.max(m, set[i]);
            }

            return m;
        }
    }

    /**
     * 查询数组最小的数据序号
     * @param set
     * @return
     */
    public static int min(int[] set) {
        if (empty(set)) {
            return 0;
        } else {
            int m = set[0];

            for(int i = 1; i < set.length; ++i) {
                m = Math.min(m, set[i]);
            }

            return m;
        }
    }

    /**
     * 拷贝数组
     * @param set
     * @return
     */
    public static String[] clone(String[] set) {
        if (null == set) {
            return null;
        } else {
            String[] clone = new String[set.length];

            for(int i = 0; i < set.length; ++i) {
                clone[i] = set[i];
            }

            return clone;
        }
    }

    /**
     * 数组转集合
     * @param set
     * @return
     */
    public static List<Integer> toArrayList(int[] set) {
        List<Integer> list = new ArrayList();
        if (!empty(set)) {
            for(int i = 0; i < set.length; ++i) {
                int val = set[i];
                list.add(new Integer(val));
            }
        }

        return list;
    }

    /**
     * 转数组
     * @param set
     * @return
     */
    public static Integer[] toArray(Object[] set) {
        Integer[] array = new Integer[set.length];
        if (!empty(set)) {
            for(int i = 0; i < set.length; ++i) {
                array[i] = NumberUtil.toInt(set[i]);
            }
        }

        return array;
    }

    /**
     * 对象数组转集合
     * @param set
     * @return
     */
    public static List<Object> toArrayList(Object[] set) {
        List<Object> list = new ArrayList();
        if (!empty(set)) {
            for(int i = 0; i < set.length; ++i) {
                list.add(set[i]);
            }
        }

        return list;
    }

    /**
     * 对象数组转字符串
     * @param set
     * @return
     */
    public static String toString(Object[] set) {
        StringBuffer str = new StringBuffer();
        if (!empty(set)) {
            for(int i = 0; i < set.length; ++i) {
                str.append(set[i]);
                if (i < set.length - 1) {
                    str.append(",");
                }
            }
        }
        return str.toString();
    }

    /**
     * 集合转字符串
     * @param collection
     * @return
     */
    public static String toString(Collection<?> collection) {
        return toString(collection, ",");
    }

    /**
     * 指定分割符号进行拼接字符串
     * @param collection
     * @param split
     * @return
     */
    public static String toString(Collection<?> collection, String split) {
        StringBuffer str = new StringBuffer();
        Iterator var3 = collection.iterator();

        while(var3.hasNext()) {
            Object o = var3.next();
            str.append(o).append(split);
        }

        return StringUtil.subStrEndDiffStr(str.toString(), split);
    }

    /**
     * int数组转字符串
     * @param set
     * @return
     */
    public static String toString(int[] set) {
        StringBuffer str = new StringBuffer();
        if (!empty(set)) {
            for(int i = 0; i < set.length; ++i) {
                str.append(set[i]);
                if (i < set.length - 1) {
                    str.append(",");
                }
            }
        }
        return str.toString();
    }

    /**
     * 字符串数组转字符串
     * @param set
     * @return
     */
    public static String toString(String[] set) {
        return toString(set);
    }

    /**
     * 字符串数组转long数组
     * @param strArray
     * @return
     */
    public static long[] toLongArray(String[] strArray) {
        if (empty((Object[])strArray)) {
            return new long[0];
        } else {
            long[] arr = new long[strArray.length];

            for(int i = 0; i < strArray.length; ++i) {
                String str = strArray[i];
                arr[i] = NumberUtil.toLong(str);
            }

            return arr;
        }
    }

    /**
     * 数组对象添加数据
     * @param srcArray
     * @param addElement
     * @return
     */
    public static int[] addIntArray(int[] srcArray, int addElement) {
        int srcLength = 0;
        if (null != srcArray) {
            srcLength = srcArray.length;
        }

        int[] dstArray = new int[srcLength + 1];

        for(int i = 0; i < srcLength; ++i) {
            dstArray[i] = srcArray[i];
        }

        dstArray[srcLength] = addElement;
        return dstArray;
    }

    /**
     * 数组添加另一个数字值
     * @param srcArray
     * @param addArray
     * @return
     */
    public static int[] addIntArray(int[] srcArray, int[] addArray) {
        int srcLength = 0;
        if (null != srcArray) {
            srcLength = srcArray.length;
        }

        int addLength = 0;
        if (null != addArray) {
            addLength = addArray.length;
        }

        int[] dstArray = new int[srcLength + addLength];

        int i;
        for(i = 0; i < srcLength; ++i) {
            dstArray[i] = srcArray[i];
        }

        for(i = 0; i < addLength; ++i) {
            dstArray[srcLength + i] = addArray[i];
        }

        return dstArray;
    }

    public static String[] addStringArray(String[] srcArray, String addElement) {
        int srcLength = 0;
        if (null != srcArray) {
            srcLength = srcArray.length;
        }

        String[] dstArray = new String[srcLength + 1];

        for(int i = 0; i < srcLength; ++i) {
            dstArray[i] = srcArray[i];
        }

        dstArray[srcLength] = addElement;
        return dstArray;
    }

    public static String[] addIntArray(String[] srcArray, String[] addArray) {
        int srcLength = 0;
        if (null != srcArray) {
            srcLength = srcArray.length;
        }

        int addLength = 0;
        if (null != addArray) {
            addLength = addArray.length;
        }

        String[] dstArray = new String[srcLength + addLength];

        int i;
        for(i = 0; i < srcLength; ++i) {
            dstArray[i] = srcArray[i];
        }

        for(i = 0; i < addLength; ++i) {
            dstArray[srcLength + i] = addArray[i];
        }

        return dstArray;
    }

    public static String[] toIntersectSet(String[] set1, String[] set2) {
        if (!empty((Object[])set1) && !empty((Object[])set2)) {
            List<String> list = new ArrayList();

            for(int i = 0; i < set1.length; ++i) {
                String s1 = set1[i];

                for(int j = 0; j < set2.length; ++j) {
                    String s2 = set2[j];
                    if (StringUtil.equals(s1, s2)) {
                        list.add(s1);
                        break;
                    }
                }
            }

            String[] set = new String[list.size()];
            list.toArray(set);
            return set;
        } else {
            return new String[0];
        }
    }

    public static double total(Collection<?> collections, String totalKey) {
        double ret = 0.0D;
        DecimalFormat df = new DecimalFormat("0.00");

        Object value;
        for(Iterator var5 = collections.iterator(); var5.hasNext(); ret += NumberUtil.toDouble(value == null ? "" : value + "")) {
            Object obj = var5.next();
            value = ObjectUtil.getProperty(obj, totalKey);
        }

        return NumberUtil.toDouble(df.format(ret));
    }

    public static String getArrFristEle(String str, ArrayList<String[]> al) {
        for(int i = 0; i < al.size(); ++i) {
            String[] strs = (String[])al.get(i);
            if (str.equals(strs[0])) {
                return strs[1];
            }
        }

        return "";
    }

    public static boolean isExist(String str, String[] strs) {
        for(int i = 0; i < strs.length; ++i) {
            if (strs[i].equalsIgnoreCase(str)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isExist(String str, Set<Object> sets) {
        String[] s = StringUtil.changeObjectArray(sets.toArray());
        return isExist(str, s);
    }
}
