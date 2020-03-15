package com.xll.common.utils.base;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Author：xuliangliang
 * @Description：集合工具类
 * @Date：1:12 上午 2020/3/16
 */
public class CollectionUtil {

    public static boolean contains(Object arr, Object obj) {
        return false;
    }

    public static int indexOf(Object arr, Object obj) {
        return -1;
    }

    public static void descartes(List<List<String>> dimvalue, List<List<String>> result, int layer, List<String> curList) {
        int i;
        ArrayList list;
        if (layer < dimvalue.size() - 1) {
            if (((List)dimvalue.get(layer)).size() == 0) {
                descartes(dimvalue, result, layer + 1, curList);
            } else {
                for(i = 0; i < ((List)dimvalue.get(layer)).size(); ++i) {
                    list = new ArrayList(curList);
                    list.add(((List)dimvalue.get(layer)).get(i));
                    descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (((List)dimvalue.get(layer)).size() == 0) {
                result.add(curList);
            } else {
                for(i = 0; i < ((List)dimvalue.get(layer)).size(); ++i) {
                    list = new ArrayList(curList);
                    list.add(((List)dimvalue.get(layer)).get(i));
                    result.add(list);
                }
            }
        }

    }
}
