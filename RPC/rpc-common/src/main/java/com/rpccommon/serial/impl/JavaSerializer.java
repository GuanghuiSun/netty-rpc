package com.rpccommon.serial.impl;

import com.rpccommon.exception.BusinessException;
import com.rpccommon.serial.Serializer;

import java.io.*;

import static com.rpccommon.constant.SerialType.JAVA;

/**
 * Java实现的序列化
 */
public class JavaSerializer implements Serializer {
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new BusinessException("java反序列化失败", e);
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new BusinessException("java序列化失败!", e);
        }
    }

    @Override
    public byte getType() {
        return JAVA.getCode();
    }
}
