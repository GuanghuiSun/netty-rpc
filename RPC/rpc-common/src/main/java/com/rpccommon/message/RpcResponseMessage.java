package com.rpccommon.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.rpccommon.constant.ReqType.RESPONSE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponseMessage implements Serializable {

    /**
     * 消息类型
     */
    private static final byte reqTYpe = RESPONSE.getCode();

    /**
     * 返回值
     */
    private Object data;

    /**
     * 消息
     */
    private String message;

    /**
     * 异常值
     */
    private Exception exceptionValue;
}
