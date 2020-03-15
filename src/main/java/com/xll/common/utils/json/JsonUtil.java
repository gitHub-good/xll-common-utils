package com.xll.common.utils.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public JsonUtil() {
    }

    public static <T> List<T> readList(String str, Class<T> cls) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, new Class[]{cls});

        try {
            return (List)mapper.readValue(str, javaType);
        } catch (Exception var4) {
            LOGGER.debug(var4.getMessage());
            return null;
        }
    }

    public static <T> T readValue(String json, Class<?> parametrized, Class<?>... parameterClasses) {
        if (json != null && json != "") {
            try {
                JavaType jt = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
                return mapper.readValue(json, jt);
            } catch (Exception var4) {
                throw new RuntimeException(var4.getMessage(), var4);
            }
        } else {
            return null;
        }
    }

    public static <T> T readValue(String json, Class<?> parametrized) {
        if (json != null && json != "") {
            try {
                return (T) mapper.readValue(json, parametrized);
            } catch (Exception var3) {
                throw new RuntimeException(var3.getMessage(), var3);
            }
        } else {
            return null;
        }
    }

    public static String writeValue(Object obj) {
        if (obj == null) {
            return "";
        } else {
            try {
                return mapper.writeValueAsString(obj);
            } catch (Exception var2) {
                throw new RuntimeException(var2.getMessage(), var2);
            }
        }
    }

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
