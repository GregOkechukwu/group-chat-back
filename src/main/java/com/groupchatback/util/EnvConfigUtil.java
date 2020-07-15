package com.groupchatback.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfigUtil {
    private final static String absolutePath  = "/Users/okechukwug/Documents/Workspace/group-chat-back/src/main/resources/env.config";
    private final static Properties properties = new Properties();

    public static String getProperty(String key)  {
        try {
            InputStream inputStream = new FileInputStream(absolutePath);
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return properties.getProperty(key);
    }
}
