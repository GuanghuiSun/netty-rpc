package com.rpccommon.serial;

import com.rpccommon.serial.impl.JavaSerializer;
import com.rpccommon.serial.impl.JsonSerializer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化管理器
 */
public class SerializerManager {
    private final static ConcurrentHashMap<Byte, Serializer> serializers = new ConcurrentHashMap<>();

    static {
        Serializer jsonSerializer = new JsonSerializer();
        Serializer javaSerializer = new JavaSerializer();
        serializers.put(jsonSerializer.getType(), jsonSerializer);
        serializers.put(javaSerializer.getType(), javaSerializer);
    }

    public static Serializer getSerializer(byte key) {
        Serializer serializer = serializers.get(key);
        if (serializer == null) {
            return new JavaSerializer();
        }
        return serializer;
    }
}
