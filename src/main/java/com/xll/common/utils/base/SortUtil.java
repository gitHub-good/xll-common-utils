package com.xll.common.utils.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SortUtil {
    public SortUtil() {
    }

    public static void main(String[] args) {
        int[] arr = new int[]{221, 4, 19, 312, 23, 442, 121};
        int[] newArr = bubbleSort(arr);
        System.out.println(newArr[newArr.length - 1]);
    }

    public static int[] bubbleSort(int[] info) {
        for(int i = 0; i < info.length; ++i) {
            for(int j = info.length - 1; j > i; --j) {
                if (info[j - 1] > info[j]) {
                    int tempVariables = info[j - 1];
                    info[j - 1] = info[j];
                    info[j] = tempVariables;
                }
            }
        }

        return info;
    }

    public static void sort(final Class cla, List list, final String orderKey, final String orderType) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;

                try {
                    String method = "get" + StringUtil.upperFirst(orderKey);
                    cla.getMethod(method, (Class[])null);
                    Method m2 = cla.getMethod(method, (Class[])null);
                    Object o1 = m2.invoke(b, (Object[])null);
                    Object o2 = m2.invoke(a, (Object[])null);
                    if (null != o1 && null != o2) {
                        if (orderType != null && "desc".equals(orderType)) {
                            ret = o1.toString().compareTo(o2.toString());
                        } else {
                            ret = o2.toString().compareTo(o1.toString());
                        }
                    } else if (null != o1 && null == o2) {
                        ret = 1;
                    } else if (null == o1 && null != o2) {
                        ret = -1;
                    }
                } catch (NoSuchMethodException var9) {
                    System.out.println(var9);
                } catch (IllegalAccessException var10) {
                    System.out.println(var10);
                } catch (InvocationTargetException var11) {
                    System.out.println(var11);
                }

                return ret;
            }
        });
    }

    public static Comparator sort(Class cla, String orderKey, final String orderType) {
        String getMethodName = "get" + StringUtil.upperFirst(orderKey);

        try {
            final Method getMethod = cla.getMethod(getMethodName);
            Comparator comp = new Comparator() {
                public int compare(Object o1, Object o2) {
                    byte val = 0;

                    try {
                        Object v1 = getMethod.invoke(o1);
                        Object v2 = getMethod.invoke(o2);
                        if (v1 instanceof Integer) {
                            if (Integer.parseInt(v1.toString()) < Integer.parseInt(v2.toString())) {
                                val = 1;
                            }
                        } else if (v1 instanceof Float) {
                            if (Float.parseFloat(v1.toString()) < Float.parseFloat(v2.toString())) {
                                val = 1;
                            }
                        } else if (v1 instanceof Double) {
                            if (Double.parseDouble(v1.toString()) < Double.parseDouble(v2.toString())) {
                                val = 1;
                            }
                        } else if (v1 instanceof String) {
                            v1.toString().compareTo(v2.toString());
                        }
                    } catch (IllegalArgumentException var6) {
                        var6.printStackTrace();
                    } catch (IllegalAccessException var7) {
                        var7.printStackTrace();
                    } catch (InvocationTargetException var8) {
                        var8.printStackTrace();
                    }

                    if (StringUtil.equals(orderType, "desc")) {
                        return val == 0 ? 1 : 0;
                    } else {
                        return val;
                    }
                }
            };
            return comp;
        } catch (SecurityException var6) {
            var6.printStackTrace();
        } catch (NoSuchMethodException var7) {
            var7.printStackTrace();
        }

        return null;
    }

    public static List<Object> sortObjectAsc(List<Object> list) {
        List<Object> sortResultList = new ArrayList();
        LinkedList<Object> tmpList = new LinkedList();

        int i;
        for(i = 0; i < list.size(); ++i) {
            Object object = list.get(i);
            tmpList.add(object);
        }

        sortBypaperTypeSort(tmpList);
        if (tmpList != null) {
            for(i = 0; i < tmpList.size(); ++i) {
                sortResultList.add(tmpList.get(i));
            }
        }

        return sortResultList;
    }

    private static void sortBypaperTypeSort(LinkedList<Object> ll) {
        new Object();
        int count = 0;

        for(int j = 0; j < ll.size() - 1; ++j) {
            boolean isContinue = false;

            for(int i = 0; i < ll.size() - 1 - count; ++i) {
                Object objectBefore = ll.get(i);
                Object objectAfter = ll.get(i + 1);
                int sortBefore = 0;
                int sortAfter = 0;
                if (sortBefore > sortAfter) {
                    ll.set(i, objectAfter);
                    ll.set(i + 1, objectBefore);
                    isContinue = true;
                }
            }

            ++count;
            if (!isContinue) {
                break;
            }
        }

    }
}
