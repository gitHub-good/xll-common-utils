package com.xll.common.utils.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PropertiesUtil {
    protected static final Log logger = LogFactory.getLog(PropertiesUtil.class);
    private static Map<String, PropertiesUtil> systemParamMap;
    private Map<String, String> param;
    private String fileName;

    private PropertiesUtil(String fileName) {
        this.fileName = fileName;
        this.init();
    }

    public static PropertiesUtil getInstance(String fileName) {
        if (systemParamMap == null) {
            systemParamMap = new HashMap();
        }

        if (systemParamMap.get(fileName) == null) {
            systemParamMap.put(fileName, new PropertiesUtil(fileName));
        }

        return (PropertiesUtil)systemParamMap.get(fileName);
    }

    private void init() {
        ResourceBundle param = ResourceBundle.getBundle(this.fileName);
        Enumeration<String> keys = param.getKeys();
        this.param = new HashMap();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            this.param.put(key, param.getString(key));
        }

    }

    public static Map read(String propertiesFile) {
        ResourceBundle rb = ResourceBundle.getBundle(propertiesFile);
        Map map = new HashMap();

        Object obj;
        Object objv;
        for(Enumeration enu = rb.getKeys(); enu.hasMoreElements(); map.put(obj, objv)) {
            obj = enu.nextElement();
            objv = rb.getObject(obj.toString());
            if (logger.isDebugEnabled()) {
                logger.debug("property [" + obj + "]:" + objv);
            }
        }

        return map;
    }

    public Map<String, String> getParam() {
        return this.param;
    }

    public String getValue(String key) {
        return (String)this.param.get(key);
    }
}
