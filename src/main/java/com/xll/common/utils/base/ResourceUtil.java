package com.xll.common.utils.base;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceUtil {
    public ResourceUtil() {
    }

    public static InputStream getResourceAsStream(String resourceName) {
        return getResourceAsStream(resourceName, (Class)null);
    }

    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);

        try {
            return url != null ? url.openStream() : null;
        } catch (IOException var4) {
            return null;
        }
    }

    public static URL getResource(String resourceName) {
        return getResource(resourceName, (Class)null);
    }

    public static URL getResource(String resourceName, Class<?> callingClass) {
        URL url = null;
        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (url == null) {
            url = ResourceUtil.class.getClassLoader().getResource(resourceName);
        }

        if (url == null && callingClass != null) {
            url = callingClass.getClassLoader().getResource(resourceName);
        }

        return url;
    }
}
