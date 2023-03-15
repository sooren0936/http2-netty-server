package com.dempsey.http2nettyserver.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Suren Kalaychyan
 */
public final class PropertyUtils {

    private static final Properties APP_PROPS = new Properties();
    private static final String RESOURCE_NAME = "application.yml";

    private PropertyUtils() {
    }

    static {
        final String resourcePath = Thread.currentThread().getContextClassLoader().getResource(RESOURCE_NAME).getPath();
        try {
            APP_PROPS.load(new FileInputStream(resourcePath));
        } catch (Exception e) {
            throw new RuntimeException("Error to load property", e);
        }
    }

    public static String findProperty(final String property) {
        return APP_PROPS.getProperty(property);
    }
}
