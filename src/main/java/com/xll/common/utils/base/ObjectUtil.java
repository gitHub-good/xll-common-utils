package com.xll.common.utils.base;

import org.apache.commons.lang.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ObjectUtil extends ClassUtils {


    public static boolean equals(Object o1, Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static String getBaseClassName(String className) {
        return StringUtil.getLastSuffix(className, ".");
    }

    public static String getPackageName(String className) {
        return StringUtil.getLastPrefix(className, ".");
    }

    public static String getMethodName(String whole) {
        return StringUtil.getLastSuffix(whole, ".");
    }

    public static int hashCode(Object o) {
        return null == o ? 0 : o.hashCode();
    }

    public static Object getObjectByConstructor(String className, Class[] intArgsClass, Object[] intArgs) {
        Object returnObj = null;

        try {
            Class classType = Class.forName(className);
            Constructor constructor = classType.getDeclaredConstructor(intArgsClass);
            constructor.setAccessible(true);
            returnObj = constructor.newInstance(intArgs);
        } catch (NoSuchMethodException var6) {
            var6.printStackTrace();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return returnObj;
    }

    public static Object getProperty(Object object, String sProperty) {
        if (null != object && null != sProperty) {
            try {
                Method method = object.getClass().getMethod("get" + StringUtil.upperFirst(sProperty), (Class[])null);
                return method.invoke(object, (Object[])null);
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static Object getFileValue(Object obj, String fieldName) {
        Object result = null;
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);

            try {
                result = field.get(obj);
            } catch (IllegalArgumentException var5) {
                var5.printStackTrace();
            } catch (IllegalAccessException var6) {
                var6.printStackTrace();
            }
        }

        return result;
    }

    public static Object useMethod(Object object, String methodName, Class[] type, Class[] value) {
        Class classType = object.getClass();
        Method method = null;
        Object fildValue = null;

        try {
            method = classType.getDeclaredMethod(methodName, type);
            method.setAccessible(true);
            fildValue = method.invoke(object, value);
        } catch (NoSuchMethodException var8) {
            var8.printStackTrace();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return fildValue;
    }

    public static Field getField(Object obj, String fieldName) {
        Field field = null;
        Class clazz = obj.getClass();

        while(clazz != Object.class) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException var5) {
                clazz = clazz.getSuperclass();
            }
        }

        return field;
    }

    public static String toString(Object o) {
        if (null == o) {
            return "";
        } else if (o instanceof Object[]) {
            return toArrayString((Object[])((Object[])o));
        } else if (o instanceof int[]) {
            return NumberUtil.toString((int[])((int[])o));
        } else if (o instanceof long[]) {
            return NumberUtil.toString((long[])((long[])o));
        } else if (o instanceof char[]) {
            return StringUtil.toString((char[])((char[])o));
        } else {
            String s = (o instanceof Calendar) ? TimeUtil.toString((Calendar) o) : o.toString();
            return s;
        }
    }

    public static String toArrayString(Object[] array) {
        if (null == array) {
            return "count=0:[]";
        } else {
            StringBuffer s = (new StringBuffer("count=")).append(array.length).append(":[ ");

            for(int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    s.append(", ");
                }

                Object obj = array[i];
                if (null != obj) {
                    if (obj instanceof Object[]) {
                        s.append(toString((Object[])((Object[])obj)));
                    } else {
                        s.append(obj.toString());
                    }
                }
            }

            s.append(" ]");
            return s.toString();
        }
    }

    public static void invokeSetGetMethod(Object object, String name, String configValue) {
        try {
            String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            String getMethodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            Method m = object.getClass().getMethod(getMethodName);
            Method method = object.getClass().getMethod(methodName, m.getReturnType());
            Object valueObj = typeConvert(m.getReturnType().getSimpleName(), configValue);
            method.invoke(object, valueObj);
        } catch (SecurityException var8) {
            var8.printStackTrace();
        } catch (NoSuchMethodException var9) {
            var9.printStackTrace();
        } catch (IllegalArgumentException var10) {
            var10.printStackTrace();
        } catch (IllegalAccessException var11) {
            var11.printStackTrace();
        } catch (InvocationTargetException var12) {
            var12.printStackTrace();
        }

    }

    public static void modifyFileValue(Object object, String filedName, Object filedValue) {
        Class classType = object.getClass();
        Field fild = null;

        try {
            fild = classType.getDeclaredField(filedName);
            fild.setAccessible(true);
            fild.set(object, filedValue);
        } catch (NoSuchFieldException var6) {
            var6.printStackTrace();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static Object typeConvert(String className, String value) {
        if (className.equals("String")) {
            return value;
        } else if (!className.equals("Integer") && !className.equals("int")) {
            if (!className.equals("long") && !className.equals("Long")) {
                if (!className.equals("boolean") && !className.equals("Boolean")) {
                    if (className.equals("Date")) {
                        return new Date(NumberUtil.toLong(value, 0L));
                    } else if (!className.equals("float") && !className.equals("Float")) {
                        return !className.equals("double") && !className.equals("Double") ? null : Double.valueOf(value);
                    } else {
                        return Float.valueOf(value);
                    }
                } else {
                    return Boolean.valueOf(value);
                }
            } else {
                return Long.valueOf(value);
            }
        } else {
            return Integer.valueOf(value);
        }
    }

    public static <T> T convertMap(Class<T> type, Map map) {
        Object obj = null;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            obj = type.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for(int i = 0; i < propertyDescriptors.length; ++i) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[]{value};
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
        } catch (Exception var10) {
        }

        return (T) obj;
    }

    protected static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }

    }

    public static Object getSimpleProperty(Object bean, String propName) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return bean.getClass().getMethod(getReadMethod(propName)).invoke(bean);
    }

    private static String getReadMethod(String name) {
        return "get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    public static Class<?> getGenericType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                return !(params[index] instanceof Class) ? Object.class : (Class)params[index];
            } else {
                throw new RuntimeException("Index outof bounds");
            }
        }
    }

    public static String getClassPath() {
        String classpath = ObjectUtil.class.getResource("/").getPath();
        return StringUtil.subStrStartDiffStr(classpath, "/");
    }
}
