package com.xll.common.utils.enums;

/**
 *
 * @Author：xuliangliang
 * @Description：时间转换格式枚举
 * @Date：1:20 上午 2020/3/16
 */
public enum  DateFormatEnum {
    FORMAT_DATE_ONE("MM/dd/yyyy"),
    FORMAT_DATE_TWO("yyyy-MM-dd");

    private String format;
    DateFormatEnum(String format){
        this.format = format;
    }

    public String getDateFormat(){
        return format;
    }


}
