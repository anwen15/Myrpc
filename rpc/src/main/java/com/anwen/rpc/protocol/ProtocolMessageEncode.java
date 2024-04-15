package com.anwen.rpc.protocol;

import com.anwen.rpc.protocol.ProtocolMessage.Header;
import com.anwen.rpc.serializer.Serializer;
import com.anwen.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 下午12:17
 * 编码
 */
public class ProtocolMessageEncode {
    /**
     * 编码
     * @param protocolMessage
     * @return
     * @throws IOException
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }
        Header header = protocolMessage.getHeader();
        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestid());
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getenum(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("没有序列化器");
        }
        Serializer serializer = SerializerFactory.getinstance(serializerEnum.getValue());
        byte[] bytes = serializer.serialize(protocolMessage.getBody());
        buffer.appendInt(bytes.length);
        buffer.appendBytes(bytes);
        return buffer;

    }
}
