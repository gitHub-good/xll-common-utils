package com.xll.common.utils.convertor;

import net.iharder.Base64;

import java.io.IOException;

/**
 *
 * @Author：xuliangliang
 * @Description：Base64转换器
 * @Date：3:02 下午 2020/3/15
 */
public class Base64Convertor implements Convertor<byte[]>{

    /**
     *解码操作
     * @param strValue
     * @return
     */
    @Override
    public byte[] parse(String strValue) {
        try {
            return Base64.decode(strValue);
        } catch (IOException var3) {
            var3.printStackTrace();
            throw new RuntimeException("Decode with Base64 error.");
        }
    }

    /**
     * 编码操作
     * @param value
     * @return
     */
    @Override
    public String format(byte[] value) {
        return Base64.encodeBytes(value);
    }
}
