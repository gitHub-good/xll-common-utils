package com.xll.common.utils.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private ObjectMapper mapper = new ObjectMapper();

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JsonUtil(JsonInclude.Include inclusion) {
        this.mapper.setSerializationInclusion(inclusion);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static JsonUtil buildNormalBinder() {
        return new JsonUtil(JsonInclude.Include.ALWAYS);
    }

    public static JsonUtil buildNonNullBinder() {
        return new JsonUtil(JsonInclude.Include.NON_NULL);
    }

    public static JsonUtil buildNonDefaultBinder() {
        return new JsonUtil(JsonInclude.Include.NON_DEFAULT);
    }

    public String getJsonToObject(Object obj) {
        if (obj == null) {
            return "";
        } else {
            try {
                return this.getMapper().writeValueAsString(obj);
            } catch (Exception var3) {
                throw new RuntimeException(var3.getMessage(), var3);
            }
        }
    }

    public <T> T getJsonToObject(String json, Class<T> clazz) {
        T object = null;
        if (StringUtil.isNotBlank(json)) {
            try {
                object = this.getMapper().readValue(json, clazz);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return object;
    }

    public <T> List<T> getJsonToList(String json, Class<T> clazz) {
        List<T> list = null;
        if (StringUtil.isNotBlank(json)) {
            try {
                JavaType javaType = this.mapper.getTypeFactory().constructParametricType(ArrayList.class, new Class[]{clazz});
                list = (List)this.getMapper().readValue(json, javaType);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return list;
    }

    public <T, E> Map<T, E> getJsonToMap(String json, Class<T> keyclazz, Class<E> valueclazz) {
        Map<T, E> object = null;
        if (StringUtil.isNotBlank(json)) {
            try {
                JavaType javaType = this.mapper.getTypeFactory().constructParametricType(Map.class, new Class[]{keyclazz, valueclazz});
                object = (Map)this.getMapper().readValue(json, javaType);
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        return object;
    }

    public String getMapToJson(Map<String, String> map) {
        List<String[]> list = new ArrayList();
        if (null != map && !map.isEmpty()) {
            Iterator var3 = map.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                String[] strS = new String[]{key, (String)map.get(key)};
                list.add(strS);
            }
        }

        return this.toJson(list);
    }

    public Object getJsonToObject(String json, Class<?> objclazz, Class<?>... pclazz) {
        Object object = null;
        if (StringUtil.isNotBlank(json)) {
            try {
                JavaType javaType = this.mapper.getTypeFactory().constructParametricType(objclazz, pclazz);
                object = this.getMapper().readValue(json, javaType);
            } catch (Exception var6) {
            }
        }

        return object;
    }

    public String toJson(Object object) {
        String json = null;

        try {
            json = this.getMapper().writeValueAsString(object);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return json;
    }

    public void setDateFormat(String pattern) {
        if (StringUtil.isNotBlank(pattern)) {
            DateFormat df = new SimpleDateFormat(pattern);
            this.getMapper().setDateFormat(df);
        }

    }
}
