package com.rpccommon.config;

import com.rpccommon.serial.Serializer;
import com.rpccommon.serial.impl.JavaSerializer;
import com.rpccommon.serial.impl.JsonSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    static Properties properties;

    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取序列化实现类
     * @return 序列化
     */
    public static Serializer getSerializer() {
        String value = properties.getProperty("serializer.type");
        if (value == null || "JAVA".equals(value)) {
            return new JavaSerializer();
        } else if ("JSON".equals(value)){
            return new JsonSerializer();
        }
        return new JavaSerializer();
    }
}
