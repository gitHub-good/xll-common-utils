package com.xll.common.utils.base;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class SerializableUtil {
    public SerializableUtil() {
    }

    public static String convert2String(Serializable obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = URLEncoder.encode(serStr, "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return serStr;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static Object convert2Object(String serStr) {
        try {
            String redStr = URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return obj;
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
