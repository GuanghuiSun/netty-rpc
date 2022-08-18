package com.rpccommon.constant;

/**
 * 消息类型
 */
public enum ReqType {

    REQUEST((byte) 1),
    RESPONSE((byte) 2);

    private byte code;

    ReqType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ReqType findType(byte code) {
        for (ReqType value : ReqType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
