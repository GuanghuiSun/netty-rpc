package com.rpccommon.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息头
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Header {
    /*
     +-------------------------------------------+
     | 魔数 2byte | 序列化算法 1byte | 请求类型 1byte |
     +-------------------------------------------+
     |       消息ID 8byte  |    数据长度 4byte      |
     +-------------------------------------------+
     */

    /**
     * 魔数 用来验证报文的身份
     */
    private short magic;
    /**
     * 序列化类型
     */
    private byte serialType;
    /**
     * 操作类型
     */
    private byte reqType;
    /**
     * 请求id
     */
    private long sequenceId;
    /**
     * 数据长度
     */
    private int length;
}
