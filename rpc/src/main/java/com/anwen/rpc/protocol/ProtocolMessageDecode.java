package com.anwen.rpc.protocol;

import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.protocol.ProtocolMessage.Header;
import com.anwen.rpc.serializer.Serializer;
import com.anwen.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 下午6:33
 * 解码
 */
public class ProtocolMessageDecode {
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        //分别从指定位置读出buffer
        Header header = new Header();
        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.Protocol_magic) {
            throw new RuntimeException("消息magic非法");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestid(buffer.getLong(5));
        header.setBodylength(buffer.getInt(13));
        //解决粘包问题,只读取指定长度的数据
        byte[] bodybuffer = buffer.getBytes(17, 17+header.getBodylength());
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getenum(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("没有序列化器");
        }
        Serializer serializer = SerializerFactory.getinstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum protocolMessageTypeEnum = ProtocolMessageTypeEnum.getenum(header.getType());
        if (protocolMessageTypeEnum == null) {
            throw new RuntimeException("没有消息类型");
        }
        switch (protocolMessageTypeEnum) {
            case request -> {
                RpcRequest rpcRequest = serializer.deserialize(bodybuffer, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            }
            case response -> {
                RpcResponse rpcResponse = serializer.deserialize(bodybuffer, RpcResponse.class);
                return new ProtocolMessage<>(header, rpcResponse);
            }
            default -> throw new RuntimeException("暂不支持该消息类型");
        }


    }
}
