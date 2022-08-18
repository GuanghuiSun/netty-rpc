package com.rpcserver;

import com.rpccommon.codec.MessageCodec;
import com.rpcserver.handler.RpcRequestMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServer {
    private String serverAddress;
    private int serverPort;

    public RpcServer(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        log.info("Server Start...");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        MessageCodec messageCodec = new MessageCodec();
        RpcRequestMessageHandler rpcRequestMessageHandler = new RpcRequestMessageHandler();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,12,4,0,0));
                            ch.pipeline().addLast(new LoggingHandler());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    log.debug("连接建立:{}",ctx);
                                    super.channelActive(ctx);
                                }
                            });
                            ch.pipeline().addLast(messageCodec);
                            ch.pipeline().addLast(rpcRequestMessageHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(this.serverPort).sync();
            log.info("Server Started Success On Port:{}", this.serverPort);
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException exception) {
            log.error("Rpc Server Error:{}", exception.getCause().getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
