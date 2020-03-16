package com.xll.common.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.UUID;

/**
 *
 * @Author：xuliangliang
 * @Description：日志
 * @Date：12:40 下午 2020/3/16
 */
public class LogUtil {
    private LogUtil(){}

    private static final ThreadLocal<String> traceInfo = new ThreadLocal<>();

    public static String getTraceInfo() {
        return traceInfo.get();
    }

    public static void setTraceInfo(String ti) {
        traceInfo.set(ti);
    }

    public static void removeTraceId() {
        traceInfo.remove();
    }

    public static String getTraceId() {
        String ti = getTraceInfo();
        return ((null != ti && !"".equals(ti)) ? ti : null);
    }

    public static void setTraceId(String logId) {
        setTraceInfo(logId);
    }

    /**
     * 日志追踪信息
     *
     * @author lichuan
     */
    static class TraceInfo {
        private String traceId;
        private String host;

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

    }

    /**
     * 获取唯一Id
     *
     * @author lichuan
     * @return
     */
    public static final String getLogId() {
        return String.valueOf(UUID.randomUUID().hashCode());
    }

    /**
     * 获取格式化的日志信息
     *
     * @author lichuan
     * @param format
     * @param argArray
     * @return
     */
    public static final String getLogMsg(String format, Object... argArray) {
        if ("".equals(format) || null == format) return "";
        FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
        return ft.getMessage() + ", logId: " + getTraceId();
    }

    /**
     * 方法参数打印
     * @param log
     * @param throwable
     * @param logId
     * @param objects
     */
    public static void logByTrace(Logger log, Throwable throwable, String logId, Object ... objects) {
        if(throwable==null || objects==null || objects.length<=0){
            return;
        }
        StackTraceElement [] stack = throwable.getStackTrace();
        StringBuilder builder=new StringBuilder().append("\n");
        builder.append("ClassName: [").append(stack[0].getClassName()).append("],").append("\n");
        builder.append("MethodName: [").append(stack[0].getMethodName()).append("],").append("\n");
        builder.append("LineNumber: [").append(stack[0].getLineNumber()).append("];").append("\n");
        builder.append("LogId: [").append(logId).append("];").append("\n");
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if(isBasicDataType(object)){
                builder.append(i).append("_param:[ ").append(object).append(" ]");
            }else{
                builder.append(i).append("_param:[ ").append(JSON.toJSONString(object)).append(" ]");
            }
            if(i!=objects.length-1){
                builder.append(",").append("\n");
            }
        }
        if(log.isInfoEnabled() || log.isDebugEnabled()){
            log.info(builder.toString());
        }
    }

    /**
     * 基础数据类型判断
     * @param obj
     * @return
     */
    private static boolean isBasicDataType(Object obj){
        return obj instanceof String||obj instanceof Float||obj instanceof Integer ||obj instanceof Long ||obj instanceof Short||obj instanceof Boolean||obj instanceof Byte||obj instanceof Character||obj instanceof Double;
    }
}
