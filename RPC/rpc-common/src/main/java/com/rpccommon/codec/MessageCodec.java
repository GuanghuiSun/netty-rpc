package com.rpccommon.codec;

import com.rpccommon.config.Config;
import com.rpccommon.constant.ReqType;
import com.rpccommon.message.Header;
import com.rpccommon.message.Message;
import com.rpccommon.message.RpcRequestMessage;
import com.rpccommon.message.RpcResponseMessage;
import com.rpccommon.serial.Serializer;
import com.rpccommon.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 编解码
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message<Object>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message<Object> message, List<Object> list) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        Header header = message.getHeader();
        // 1. 2 字节的魔数
        out.writeShort(header.getMagic());
        // 2. 1 字节的序列化算法,
        Serializer serializer = Config.getSerializer();
        out.writeByte(serializer.getType());
        // 3. 1 字节的请求类型
        out.writeByte(message.getHeader().getReqType());
        // 4. 4 字节的消息ID
        out.writeLong(header.getSequenceId());
        // 5. 获取内容的字节数组
        byte[] bytes = serializer.serialize(message.getContent());
        // 6. 4 个字节数据长度
        header.setLength(bytes.length);
        out.writeInt(bytes.length);
        // 7. 写入内容
        out.writeBytes(bytes);
        list.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        // 1. 2 字节的魔数
        short magicNum = in.readShort();
        // 2. 1 字节的序列化算法,
        byte serialType = in.readByte(); // 0 或 1
        // 3. 1 字节的请求类型
        byte reqType = in.readByte();
        // 4. 4 字节的消息ID
        long sequenceId = in.readLong();
        // 5. 4 个字节数据长度
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        Serializer serializer = SerializerManager.getSerializer(serialType);
        Header header = new Header(magicNum, serialType, reqType, sequenceId, length);
        ReqType req = ReqType.findType(reqType);
        //确定消息类型
        switch (req) {
            case REQUEST:
                RpcRequestMessage message = serializer.deserialize(RpcRequestMessage.class, bytes);
                Message<RpcRequestMessage> request = new Message<>(header, message);
                list.add(request);
                break;
            case RESPONSE:
                RpcResponseMessage responseMessage = serializer.deserialize(RpcResponseMessage.class, bytes);
                Message<RpcResponseMessage> resp = new Message<>(header, responseMessage);
                list.add(resp);
                break;
        }
    }
}
