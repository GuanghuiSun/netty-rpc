package com.rpccommon.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

import static com.rpccommon.constant.ReqType.REQUEST;

@Data
@AllArgsConstructor
public class RpcRequestMessage implements Serializable {

    /**
     * 消息类型
     */
    private static final byte reqTYpe = REQUEST.getCode();
    /**
     * 方法全限定名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数列表
     */
    private Object[] param;

    /**
     * 返回类型
     */
    private Class<?> returnType;

    /**
     * 参数类型
     */
    private Class[] paramType;
}
