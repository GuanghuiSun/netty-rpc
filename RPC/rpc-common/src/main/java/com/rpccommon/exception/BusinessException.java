package com.rpccommon.exception;

import java.io.Serializable;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -836387913753259763L;

    private Throwable cause;

    public BusinessException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public BusinessException(Throwable cause) {
        super(cause.getMessage());
    }

}