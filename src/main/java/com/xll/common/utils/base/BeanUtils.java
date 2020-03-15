package com.xll.common.utils.base;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.dozer.DozerBeanMapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @Author：xuliangliang
 * @Description：Bean工具类
 * @Date：4:20 下午 2020/3/15
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
    private static final DozerBeanMapper mapper = new DozerBeanMapper();

    /**
     * 反射获取对象中的属性
     * @param object
     * @param propertyName
     * @return
     * @throws NoSuchFieldException
     */
    public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
        return getDeclaredField(object.getClass(), propertyName);
    }

    public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
        Class superClass = clazz;

        while(superClass != Object.class) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }

    public static Object getNameProperty(Object object, String propertyName) {
        Field field = null;

        try {
            field = getDeclaredField(object, propertyName);
        } catch (NoSuchFieldException var7) {
        }

        Object result = null;
        if (null != field) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            try {
                result = field.get(object);
            } catch (Exception var6) {
            }

            field.setAccessible(accessible);
        }

        return result;
    }

    public static Object newForceGetProperty(Object object, String propertyName) {
        if (null == object) {
            return null;
        } else if (StringUtil.isBlank(propertyName)) {
            return null;
        } else {
            String[] s = propertyName.split("\\.");
            if (null == s) {
                return null;
            } else {
                for(int i = 0; i < s.length; ++i) {
                    object = forceGetProperty(object, s[i]);
                }

                return object;
            }
        }
    }

    public static Object forceGetProperty(Object object, String propertyName) {
        Object result = null;

        try {
            if (object instanceof Map) {
                result = ((Map)object).get(propertyName);
            } else {
                result = getObjValue(object, propertyName, (Object)null);
            }
        } catch (Exception var4) {
        }

        return result;
    }

    public static void setNameProperty(Object object, String propertyName, Object newValue) throws NoSuchFieldException {
        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        try {
            field.set(object, newValue);
        } catch (Exception var6) {
        }

        field.setAccessible(accessible);
    }

    public static void forceSetProperty(Object object, String propertyName, Object newValue) {
        if (null != object && !StringUtil.isBlank(propertyName)) {
            String[] s = propertyName.split("\\.");
            if (null != s) {
                for(int i = 0; i < s.length - 1; ++i) {
                    object = forceGetProperty(object, s[i]);
                }

                try {
                    if (object instanceof Map) {
                        ((Map)object).put(propertyName, newValue);
                    } else {
                        setObjValue(object, propertyName, newValue);
                    }
                } catch (Exception var5) {
                }

            }
        }
    }

    public static Object invokePrivateMethod(Object object, String methodName, Object... params) throws NoSuchMethodException {
        Class[] types = new Class[params.length];

        for(int i = 0; i < params.length; ++i) {
            types[i] = params[i].getClass();
        }

        Class clazz = object.getClass();
        Method method = null;
        Class superClass = clazz;

        while(superClass != Object.class) {
            try {
                method = superClass.getDeclaredMethod(methodName, types);
                break;
            } catch (NoSuchMethodException var10) {
                superClass = superClass.getSuperclass();
            }
        }

        if (method == null) {
            throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);
        } else {
            boolean accessible = method.isAccessible();
            method.setAccessible(true);
            Object result = null;

            try {
                result = method.invoke(object, params);
            } catch (Exception var9) {
            }

            method.setAccessible(accessible);
            return result;
        }
    }

    public static Method transferMethoder(String classpath, String methodname, Class[] types) {
        try {
            Class clazz = Class.forName(classpath);
            return clazz != Object.class ? clazz.getMethod(methodname, types) : null;
        } catch (Exception var5) {
            return null;
        }
    }

    public static Method transferMethoder(Object obj, String methodname, Class[] types) {
        try {
            Class clazz = obj.getClass();
            return clazz != Object.class ? clazz.getMethod(methodname, types) : null;
        } catch (Exception var5) {
            return null;
        }
    }

    public static Field[] getObjProperty(Object obj) {
        Class c = obj.getClass();
        Field[] field = c.getDeclaredFields();
        return field;
    }

    public static void copyPropertiesSafe(Object dest, Object src) {
        if (null != src && null != dest) {
            try {
                mapper.map(src, dest);
            } catch (Throwable var3) {
                var3.printStackTrace();
            }

        }
    }

    public static void copySupperPropertys(Object arg0, Object arg1) throws Exception {
        if (null != arg0 && null != arg1) {
            Object value = null;
            if (arg1 instanceof Map) {
                Iterator var3 = ((Map)arg1).keySet().iterator();

                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    value = forceGetProperty(arg1, key);
                    forceSetProperty(arg0, key, value);
                }
            } else {
                Field[] field = getObjSupperProperty(arg1);
                if (null != field) {
                    for(int i = 0; i < field.length; ++i) {
                        value = forceGetProperty(arg1, field[i].getName());
                        forceSetProperty(arg0, field[i].getName(), value);
                    }
                }
            }

        } else {
            throw new Exception("参数为空");
        }
    }

    public static void copyAllPropertys(Object arg0, Object arg1) throws Exception {
        if (null != arg0 && null != arg1) {
            Object value = null;
            if (arg1 instanceof Map) {
                Iterator var3 = ((Map)arg1).keySet().iterator();

                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    value = forceGetProperty(arg1, key);
                    forceSetProperty(arg0, key, value);
                }
            } else {
                Field[] field = getObjAllProperty(arg1);
                if (null != field) {
                    for(int i = 0; i < field.length; ++i) {
                        if ("goodsSudate".equals(field[i].getName())) {
                            System.out.println(field[i].getName());
                            System.out.println("==");
                        }

                        value = forceGetProperty(arg1, field[i].getName());
                        forceSetProperty(arg0, field[i].getName(), value);
                    }
                }
            }

        } else {
            throw new Exception("参数为空");
        }
    }

    public static void copyImplPropertys(Object arg0, Object arg1) throws Exception {
        if (null != arg0 && null != arg1) {
            Object value = null;
            if (arg1 instanceof Map) {
                Iterator var3 = ((Map)arg1).keySet().iterator();

                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    value = forceGetProperty(arg1, key);
                    forceSetProperty(arg0, key, value);
                }
            } else {
                Field[] field = getObjProperty(arg1);
                if (null != field) {
                    for(int i = 0; i < field.length; ++i) {
                        value = forceGetProperty(arg1, field[i].getName());
                        forceSetProperty(arg0, field[i].getName(), value);
                    }
                }
            }

        } else {
            throw new Exception("参数为空");
        }
    }

    public static Field[] getObjSupperProperty(Object obj) {
        Class c = obj.getClass();
        Class supper = c.getSuperclass();
        List<Field> list = new ArrayList();
        if (null != supper) {
            for(Class superClass = supper; superClass != Object.class; superClass = superClass.getSuperclass()) {
                Field[] fieldchild = superClass.getDeclaredFields();
                if (null != fieldchild) {
                    Field[] var6 = fieldchild;
                    int var7 = fieldchild.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        Field field2 = var6[var8];
                        list.add(field2);
                    }
                }
            }
        }

        Field[] field = new Field[list.size()];
        field = (Field[])list.toArray(field);
        return field;
    }

    public static Field[] getObjOpSupperProperty(Object obj) {
        Class c = obj.getClass();
        Class supper = c.getSuperclass();
        List<Field> list = new ArrayList();
        if (null != supper) {
            for(Class superClass = supper; superClass != Object.class; superClass = superClass.getSuperclass()) {
                Field[] fieldchild = superClass.getDeclaredFields();
                if (null != fieldchild) {
                    Field[] var6 = fieldchild;
                    int var7 = fieldchild.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        Field field2 = var6[var8];
                        list.add(field2);
                    }
                }
            }
        }

        Field[] field = new Field[list.size()];
        field = (Field[])list.toArray(field);
        return field;
    }

    public static Field[] getObjAllProperty(Object obj) {
        List<Field> list = new ArrayList();

        for(Class superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fieldchild = superClass.getDeclaredFields();
            if (null != fieldchild) {
                Field[] var4 = fieldchild;
                int var5 = fieldchild.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Field field2 = var4[var6];
                    list.add(field2);
                }
            }
        }

        Field[] field = new Field[list.size()];
        field = (Field[])list.toArray(field);
        return field;
    }

    public static Field[] getObjAllOpProperty(Object obj) {
        List<Field> list = new ArrayList();

        for(Class superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fieldchild = superClass.getDeclaredFields();
            if (null != fieldchild) {
                Field[] var4 = fieldchild;
                int var5 = fieldchild.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Field field2 = var4[var6];
                    list.add(field2);
                }
            }
        }

        Field[] field = new Field[list.size()];
        field = (Field[])list.toArray(field);
        return field;
    }

    public static String getProNameMethod(String proName) {
        String methodName = "";
        if (StringUtil.isNotBlank(proName)) {
            methodName = "get" + StringUtil.upperFirst(proName);
        }

        return methodName;
    }

    public static String getProSetNameMethod(String proName) {
        String methodName = "";
        if (StringUtil.isNotBlank(proName)) {
            methodName = "set" + StringUtil.upperFirst(proName);
        }

        return methodName;
    }

    public static Object getObjValue(Object obj, String name, Object defObj) {
        Object valueObj = null;
        String methodName = getProNameMethod(name);
        Method method = transferMethoder(obj, methodName, new Class[0]);
        if (null != method) {
            try {
                valueObj = method.invoke(obj);
                if (null == valueObj) {
                    valueObj = defObj;
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

        return valueObj;
    }

    public static void setObjValue(Object obj, String name, Object defObj) {
        String methodName = getProSetNameMethod(name);

        try {
            Field field = getDeclaredField(obj, name);
            Class fclass = field.getType();
            Object valueobj = getValueByType(fclass.getName(), defObj);
            Class[] types = new Class[]{fclass};
            Method method = transferMethoder(obj, methodName, types);
            if (null != method) {
                method.invoke(obj, valueobj);
            }
        } catch (Exception var9) {
        }

    }

    public static Object getValueByType(String className, Object defObj) {
        Object obj = null;
        if (className.indexOf("String") >= 0) {
            if (null == defObj) {
                obj = null;
            } else {
                obj = defObj + "";
            }
        } else if (className.indexOf("int") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "0";
            }

            obj = Long.valueOf(String.valueOf(defObj)).intValue();
        } else if (className.indexOf("Long") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "0";
            }

            obj = Long.valueOf(String.valueOf(defObj));
        } else if (className.indexOf("Double") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "0";
            }

            obj = Double.valueOf(String.valueOf(defObj));
        } else if (className.indexOf("double") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "0";
            }

            obj = Double.valueOf(String.valueOf(defObj));
        } else if (className.indexOf("Date") >= 0) {
            if (null != defObj && StringUtil.isNotBlank(String.valueOf(defObj))) {
                obj = TimeUtil.toCalendar(String.valueOf(defObj)).getTime();
                if (obj == null) {
                    obj = defObj;
                }
            }
        } else if (className.indexOf("Integer") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "0";
            }

            obj = Integer.valueOf(String.valueOf(defObj));
        } else if (className.indexOf("boolean") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "false";
            }

            if ("true".equals(String.valueOf(defObj))) {
                obj = true;
            } else {
                obj = false;
            }
        } else if (className.indexOf("Boolean") >= 0) {
            if (StringUtil.isBlank(String.valueOf(defObj))) {
                defObj = "false";
            }

            if ("true".equals(String.valueOf(defObj))) {
                obj = true;
            } else {
                obj = false;
            }
        }

        return obj;
    }

    public static void setObjValue(Object obj, String name, String defObj) {
        String methodName = getProSetNameMethod(name);

        try {
            Field field = getDeclaredField(obj, name);
            Class fclass = field.getType();
            Class[] types = new Class[]{fclass};
            Method method = transferMethoder(obj, methodName, types);
            if (null != method) {
                method.invoke(obj, getStringToType(fclass, defObj));
            }
        } catch (Exception var8) {
        }

    }

    public static Object getObject(Object obj, String name, String defObj) {
        String var3 = getProSetNameMethod(name);

        try {
            Field field = getDeclaredField(obj, name);
            Class fclass = field.getType();
            Class[] var10000 = new Class[]{fclass};
            return getStringToType(fclass, defObj);
        } catch (Exception var7) {
            return null;
        }
    }

    public static String getObjectHql(Object obj, String name, List<Object> paramlist, Object value) {
        String var4 = getProSetNameMethod(name);

        try {
            Field field = getDeclaredField(obj, name);
            Class fclass = field.getType();
            Class[] var10000 = new Class[]{fclass};
            return getStringToHql(fclass, name, paramlist, value);
        } catch (Exception var8) {
            return null;
        }
    }

    public static Object getStringToType(Class typeClass, String value) {
        Object obj = null;
        if (typeClass.equals(String.class)) {
            if (null != value && !StringUtil.isBlank(value)) {
                obj = String.valueOf(value);
            } else {
                obj = "";
            }
        } else if (typeClass.equals(Double.class)) {
            if (null != value && !StringUtil.isBlank(value)) {
                obj = Double.valueOf(value);
            } else {
                obj = 0.0D;
            }
        } else if (typeClass.equals(Integer.class)) {
            if (null != value && !StringUtil.isBlank(value)) {
                obj = Integer.valueOf(value);
            } else {
                obj = 0;
            }
        } else if (typeClass.equals(Date.class)) {
            if (null != value && !StringUtil.isBlank(value)) {
                obj = TimeUtil.toCalendar(value).getTime();
            } else {
                obj = null;
            }
        } else if (typeClass.equals(Long.class)) {
            if (null != value && !StringUtil.isBlank(value)) {
                obj = Long.valueOf(value);
            } else {
                obj = 0L;
            }
        } else {
            obj = 0;
        }

        return obj;
    }

    public static String getStringToHql(Class typeClass, String name, List<Object> paramlist, Object value) {
        String obj = null;
        if (typeClass.equals(String.class)) {
            obj = "'--'";
            paramlist.add(null != value && !"".equals(value) ? value : "--");
        } else if (typeClass.equals(Double.class)) {
            obj = "0";
            paramlist.add(null != value && !"".equals(value) ? value : 0.0D);
        } else if (typeClass.equals(Integer.class)) {
            obj = "0";
            paramlist.add(null != value && !"".equals(value) ? value : 0);
        } else if (typeClass.equals(Date.class)) {
            obj = "to_date('1991.01.01','yyyy.mm.dd')";
            paramlist.add(null != value && !"".equals(value) ? value : new Date("1991.01.01"));
        } else if (typeClass.equals(Long.class)) {
            obj = "0";
            paramlist.add(null != value && !"".equals(value) ? value : 0L);
        } else {
            obj = "0";
            paramlist.add(null != value && !"".equals(value) ? value : 0);
        }

        return obj;
    }

    public static void copyPropertiesNotNull(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        int i;
        String name;
        Object value;
        if (orig instanceof DynaBean) {
            DynaProperty[] origDescriptors = ((DynaBean)orig).getDynaClass().getDynaProperties();

            for(i = 0; i < origDescriptors.length; ++i) {
                name = origDescriptors[i].getName();
                if (propertyUtilsBean.isWriteable(dest, name)) {
                    value = ((DynaBean)orig).get(name);
                    if (value != null) {
                        copyProperty(dest, name, value);
                    }
                }
            }
        } else if (orig instanceof Map) {
            Iterator names = ((Map)orig).keySet().iterator();

            while(names.hasNext()) {
                name = (String)names.next();
                if (propertyUtilsBean.isWriteable(dest, name)) {
                    value = ((Map)orig).get(name);
                    if (value != null) {
                        copyProperty(dest, name, value);
                    }
                }
            }
        } else {
            PropertyDescriptor[] origDescriptors = propertyUtilsBean.getPropertyDescriptors(orig);

            for(i = 0; i < origDescriptors.length; ++i) {
                name = origDescriptors[i].getName();
                if (!"class".equals(name) && propertyUtilsBean.isReadable(orig, name) && propertyUtilsBean.isWriteable(dest, name)) {
                    try {
                        value = propertyUtilsBean.getSimpleProperty(orig, name);
                        if (value != null) {
                            copyProperty(dest, name, value);
                        }
                    } catch (NoSuchMethodException var7) {
                    }
                }
            }
        }

    }

    public static void copyMap2Pojo(Object targetBean, Map dataMap, boolean ignoreEmptyString) throws Exception {
        try {
            PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(targetBean);

            for(int i = 0; i < origDescriptors.length; ++i) {
                String name = origDescriptors[i].getName();
                if (!name.equals("class") && PropertyUtils.isWriteable(targetBean, name)) {
                    Object obj = dataMap.get(name);
                    if (obj != null && (obj.toString().trim().length() != 0 || !ignoreEmptyString)) {
                        obj = convertValue(origDescriptors[i], obj);
                        copyProperty(targetBean, name, obj);
                    }
                }
            }

        } catch (Exception var7) {
            var7.printStackTrace();
            throw var7;
        }
    }

    public static Map copyPojo2Map(Object dataBean, boolean ignoreEmptyString) throws Exception {
        HashMap targetMap = new HashMap();

        try {
            PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(dataBean);

            for(int i = 0; i < origDescriptors.length; ++i) {
                String name = origDescriptors[i].getName();
                if (!"class".equals(name)) {
                    Object obj = PropertyUtils.getProperty(dataBean, name);
                    if (obj != null && (obj.toString().trim().length() != 0 || !ignoreEmptyString)) {
                        if (obj instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            obj = sdf.format((Date)obj);
                        }

                        targetMap.put(name, obj);
                    }
                }
            }

            return targetMap;
        } catch (Exception var8) {
            var8.printStackTrace();
            throw var8;
        }
    }

    public static Map getProperties(Object bean) throws Exception {
        if (bean == null) {
            return null;
        } else {
            HashMap dataMap = new HashMap();

            try {
                PropertyDescriptor[] origDescriptors = PropertyUtils.getPropertyDescriptors(bean);

                for(int i = 0; i < origDescriptors.length; ++i) {
                    String name = origDescriptors[i].getName();
                    if (!name.equals("class") && PropertyUtils.isReadable(bean, name)) {
                        Object obj = PropertyUtils.getProperty(bean, name);
                        if (obj != null) {
                            obj = convertValue(origDescriptors[i], obj);
                            dataMap.put(name, obj);
                        }
                    }
                }

                return dataMap;
            } catch (Exception var6) {
                var6.printStackTrace();
                throw var6;
            }
        }
    }

    private static Object convertValue(PropertyDescriptor origDescriptor, Object obj) throws Exception {
        if (obj == null) {
            return null;
        } else if (obj.toString().trim().length() == 0) {
            return null;
        } else {
            if (origDescriptor.getPropertyType() == Date.class) {
                if (obj instanceof Date) {
                    return obj;
                }

                obj = TimeUtil.toCalendar(obj.toString()).getTime();
            }

            return obj;
        }
    }
}
