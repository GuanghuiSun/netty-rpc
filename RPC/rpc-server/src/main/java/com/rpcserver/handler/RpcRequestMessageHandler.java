package com.rpcserver.handler;

import com.rpccommon.message.Header;
import com.rpccommon.message.Message;
import com.rpccommon.message.RpcRequestMessage;
import com.rpccommon.message.RpcResponseMessage;
import com.rpcserver.factory.ServicesFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

import static com.rpccommon.constant.ReqType.RESPONSE;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<Message<RpcRequestMessage>> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<RpcRequestMessage> message) throws Exception {
        log.debug("message:{}", message);
        Message<Object> responseMessage = new Message<>();
        RpcResponseMessage response = new RpcResponseMessage();
        Header reqHeader = message.getHeader();
        RpcRequestMessage content = message.getContent();
        Header respHeader = new Header();
        respHeader.setMagic(reqHeader.getMagic());
        respHeader.setSerialType(reqHeader.getSerialType());
        respHeader.setSequenceId(reqHeader.getSequenceId());
        respHeader.setReqType(RESPONSE.getCode());
        responseMessage.setHeader(respHeader);
        try {
            Class<?> interfaceName = Class.forName(content.getInterfaceName());
            Object service = ServicesFactory.getService(interfaceName);
            Method method = service.getClass().getMethod(content.getMethodName(), content.getParamType());
            Object data = method.invoke(service, content.getParam());
            log.debug("data:{}", data);
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错:" + msg));
        }
        responseMessage.setContent(response);
        ctx.writeAndFlush(responseMessage);
        log.debug("响应成功!, Message:{}", responseMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
