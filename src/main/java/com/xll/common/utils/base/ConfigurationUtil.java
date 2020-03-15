package com.xll.common.utils.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 *
 * @Author：xuliangliang
 * @Description：配置工具
 * @Date：3:18 下午 2020/3/15
 */
public class ConfigurationUtil {
    private static final String[] CONFIGS = new String[]{"scm-utilities", "scm-utilities-ext"};
    public static final String ENCRYPT_ALGORITHM_KEY = "nebula.encrypt.algorithm";
    public static final String SIGNATURE_ALGORITHM_KEY = "nebula.signature.algorithm";
    public static final String RANDOM_ALGORITHM_KEY = "nebula.random.algorithm";
    public static final String HASH_ALGORITHM_KEY = "nebula.hash.algorithm";
    public static final String DIGEST_ALGORITHM_KEY = "nebula.digest.algorithm";
    public static final String DEFAULT_ENCODING = "UTF-8";
    private List<Properties> properties;
    private static ConfigurationUtil instance = null;

    private ConfigurationUtil() {
        this.properties = loadConfiguration(CONFIGS);
    }

    /**
     * 实例化一个对象
     * @return
     */
    public static ConfigurationUtil getInstance() {
        if (instance == null) {
            instance = new ConfigurationUtil();
        }

        return instance;
    }

    public String getUtilityConfiguration(String propertyName) {
        return getConfigurationValue(propertyName, this.properties);
    }

    public static List<Properties> loadConfiguration(String... configs) {
        List<Properties> props = new ArrayList();
        String[] var2 = configs;
        int var3 = configs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String config = var2[var4];
            InputStream is = ResourceUtil.getResourceAsStream(config + ".properties", ConfigurationUtil.class);
            if (is != null) {
                Properties prop = new Properties();

                try {
                    prop.load(is);
                    props.add(prop);
                } catch (IOException var9) {
                    var9.printStackTrace();
                }
            }
        }

        return props;
    }

    public static String getConfigurationValue(String propertyName, List<Properties> properties) {
        if (properties != null && properties.size() != 0) {
            String result = null;
            Iterator var3 = properties.iterator();

            while(var3.hasNext()) {
                Properties prop = (Properties)var3.next();
                result = prop.getProperty(propertyName);
                if (result != null) {
                    break;
                }
            }

            return result;
        } else {
            return null;
        }
    }
}

