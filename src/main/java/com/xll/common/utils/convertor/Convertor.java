package com.xll.common.utils.convertor;

/**
 *
 * @Author：xuliangliang
 * @Description：Base64编码和解码接口
 * @Date：3:07 下午 2020/3/15
 */
public interface Convertor<T> {

    T parse(String var1);

    String format(T var1);
}