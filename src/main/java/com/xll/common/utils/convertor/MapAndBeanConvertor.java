package com.xll.common.utils.convertor;

import com.xll.common.utils.base.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapAndBeanConvertor {

    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    public MapAndBeanConvertor() {
    }

    public static Map<String, Object> toMap(Object thisObj) {
        HashMap map = new HashMap();

        try {
            Class c = Class.forName(thisObj.getClass().getName());
            Method[] m = c.getMethods();

            for(int i = 0; i < m.length; ++i) {
                String method = m[i].getName();
                if (method.startsWith("get") && !method.equals("getClass")) {
                    try {
                        Object value = m[i].invoke(thisObj);
                        if (value != null) {
                            String key = method.substring(3);
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                            map.put(key, value);
                        }
                    } catch (Exception var8) {
                        logger.error("error:" + method);
                    }
                }
            }
        } catch (Exception var9) {
            logger.error("", var9);
        }

        return map;
    }

    public static void main(String[] args) throws Exception {
        TMapTest tt = new TMapTest();
        tt.setId(3L);
        tt.setName("sdf3d在的33在的在");
        Map<String, Object> map = toMap(tt);
        System.out.println(map.get("name"));
    }
}
