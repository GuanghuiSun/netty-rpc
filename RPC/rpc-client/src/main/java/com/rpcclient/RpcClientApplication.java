package com.rpcclient;

import com.rpcapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RpcClientApplication {

    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(RpcClientApplication.class, args);
        RpcClientProxy proxy = new RpcClientProxy();
        UserService service = proxy.getProxyService(UserService.class, "localhost", 9999);
        String result = service.saveUser("张三");
        log.debug("远程调用响应结果:{}",result);
        String userId = service.getUserId("李四", 123L);
        log.debug("远程调用响应结果:{}", userId);
    }
}
