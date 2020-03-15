package com.xll.common.utils.base;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptUtil {
    public static final String ERROR = "0";
    public static final String OK = "1";
    public static final String EXCEPTION = "error";

    public ScriptUtil() {
    }

    public static String evel(String str) {
        return evel(str, "JavaScript");
    }

    public static String evel(String str, String shortname) {
        String s = "";
        if (StringUtil.isBlank(str)) {
            return s;
        } else {
            if (StringUtil.isBlank(shortname)) {
                shortname = "JavaScript";
            }

            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName(shortname);

            try {
                str = str.replace("\r", "").replace("\n", "").replace("\r\n", "");
                Object obj = engine.eval(str);
                if (null != obj) {
                    s = obj.toString();
                }
            } catch (Exception var6) {
                var6.printStackTrace();
                s = "error";
            }

            return s;
        }
    }

    public static void main(String[] args) {
        String s = "var msg;if('\r头条摘要头条摘要头条摘要头条摘要头条'.replace('\\r','').replace('\\r','').length>40){msg='头条摘要头条摘要头条摘要头条摘要头条'.replace(/<[^>]+>/g,'').substring(0,40)+'...';}else{msg='头条摘要头条摘要头条摘要头条摘要头条'.replace(/<[^>]+>/g,'');}msg;";
        System.out.println(evel(s));
    }
}
