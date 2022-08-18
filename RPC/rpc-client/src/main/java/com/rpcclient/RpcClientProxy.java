package com.rpcclient;

import com.rpcclient.proxy.RpcInvokerProxy;

import java.lang.reflect.Proxy;

/**
 * 客户端代理类
 */
public class RpcClientProxy {

    public <T> T getProxyService(Class<T> serviceClass, String serverAddress, int serverPort) {
        ClassLoader classLoader = serviceClass.getClassLoader();
        return (T) Proxy.newProxyInstance(classLoader,
                new Class[]{serviceClass},
                new RpcInvokerProxy(serverAddress, serverPort));
    }
}
