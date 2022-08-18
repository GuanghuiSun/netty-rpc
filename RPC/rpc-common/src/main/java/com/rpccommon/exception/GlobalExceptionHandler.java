package com.rpccommon.exception;

import com.rpccommon.message.RpcResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


//    @ExceptionHandler(BusinessException.class)
//    public RpcResponseMessage businessExceptionHandler(BusinessException e) {
//        log.error("businessException:" + e.getMessage(), e);
//        return new RpcResponseMessage()
//    }

}
