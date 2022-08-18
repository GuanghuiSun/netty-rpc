package com.rpccommon.constant;


/**
 * 序列化类型
 */
public enum SerialType {

    JSON((byte)1),
    JAVA((byte)2);

    private byte code;

    SerialType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

}
