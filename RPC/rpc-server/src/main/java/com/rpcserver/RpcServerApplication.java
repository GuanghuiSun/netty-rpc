package com.rpcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
        new RpcServer("localhost", 9999).start();
    }

}
