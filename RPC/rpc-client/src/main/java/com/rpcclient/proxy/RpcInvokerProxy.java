package com.rpcclient.proxy;

import com.rpcclient.RpcClient;
import com.rpcclient.generator.SequenceIdGenerator;
import com.rpcclient.handler.RpcResponseHandler;
import com.rpccommon.config.Config;
import com.rpccommon.message.Header;
import com.rpccommon.message.Message;
import com.rpccommon.message.RpcRequestMessage;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.rpccommon.constant.ReqType.REQUEST;
import static com.rpccommon.constant.RpcConstant.MAGIC;

/**
 * 代理类 远程调用方法
 */
@Slf4j
public class RpcInvokerProxy implements InvocationHandler {
    private String serverAddress;
    private int serverPort;

    public RpcInvokerProxy(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Begin Invoke Target Server, Address:{}, Port:{}", serverAddress, serverPort);
        Message<RpcRequestMessage> message = new Message<>();
        long sequenceId = SequenceIdGenerator.nextId();
        Header header = new Header(MAGIC, Config.getSerializer().getType(), REQUEST.getCode(), sequenceId, 0);
        message.setHeader(header);
        RpcRequestMessage content = new RpcRequestMessage(
            method.getDeclaringClass().getName(),
                method.getName(),
                args,
                method.getReturnType(),
                method.getParameterTypes()
        );
        message.setContent(content);
        RpcClient client = new RpcClient(this.serverAddress, this.serverPort);
        Channel channel = client.getChannel();
        channel.writeAndFlush(message);
        //准备一个Promise接收结果
        DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
        RpcResponseHandler.PROMISES.put(sequenceId, promise);
        //等待promise结果
        promise.await();
        RpcResponseHandler.PROMISES.remove(sequenceId);
        if (promise.isSuccess()) {
            log.info("远程调用成功!");
            return promise.getNow();
        } else {
            log.error("远程调用失败!" + promise.cause().getMessage());
        }
        return null;
    }
}
