package com.rpcclient.handler;

import com.rpccommon.message.Message;
import com.rpccommon.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ChannelHandler.Sharable
public class RpcResponseHandler extends SimpleChannelInboundHandler<Message<RpcResponseMessage>> {

    public static final Map<Long, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<RpcResponseMessage> message) throws Exception {
        log.debug("Client Received A Response Message:{}", message);
        long sequenceId = message.getHeader().getSequenceId();
        Promise<Object> promise = PROMISES.get(sequenceId);
        if (promise != null) {
            RpcResponseMessage content = message.getContent();
            Exception exceptionValue = content.getExceptionValue();
            if (exceptionValue != null) {
                promise.setFailure(exceptionValue);
            } else {
                promise.setSuccess(content.getData());
            }
        }
    }
}
