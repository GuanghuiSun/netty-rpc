package com.rpccommon.serial.impl;

import com.alibaba.fastjson.JSON;
import com.rpccommon.constant.SerialType;
import com.rpccommon.serial.Serializer;

import java.nio.charset.StandardCharsets;

/**
 * JSON序列化
 */
public class JsonSerializer implements Serializer {
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(new String(bytes), clazz);
    }

    @Override
    public <T> byte[] serialize(T object) {
        return JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte getType() {
        return SerialType.JSON.getCode();
    }
}
