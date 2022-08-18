package com.rpcclient;

import com.rpcclient.handler.RpcResponseHandler;
import com.rpcclient.proxy.RpcInvokerProxy;
import com.rpccommon.codec.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端
 * 用于建立channel
 */
@Slf4j
public class RpcClient {
    private Channel channel;
    private final Object LOCK = new Object();

    private String serverAddress;
    private int serverPort;

    public RpcClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    private void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        MessageCodec messageCodec = new MessageCodec();
        Bootstrap bootstrap = new Bootstrap();
        RpcResponseHandler rpcResponseHandler = new RpcResponseHandler();
        bootstrap.channel(NioSocketChannel.class)
                .group(group)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0));
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(messageCodec);
                        ch.pipeline().addLast(rpcResponseHandler);
                    }
                });
        try {
            channel = bootstrap.connect(this.serverAddress, this.serverPort).sync().channel();
            channel.closeFuture().addListener(future -> group.shutdownGracefully());
        } catch (InterruptedException exception) {
            log.error("Client Error:{}", exception.getCause().getMessage());
        }
    }
}
