package com.anwen.rpc.server.tcp;

import com.anwen.rpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 下午11:54
 * 使用recordparser对原来buffer处理能力进行增强
 */
public class tcpbufferhanderwrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    public tcpbufferhanderwrapper(Handler<Buffer> handler) {
        this.recordParser = initRecordp(handler);
    }


    @Override
    public void handle(Buffer event) {
        recordParser.handle(event);

    }

    private RecordParser initRecordp(Handler<Buffer> handler) {
        //构造parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.Message_header_length);
        parser.setOutput(new Handler<Buffer>() {
            //初始化size大小
            int size = -1;
            //一次完整的读取
            Buffer resultbuffer = Buffer.buffer();
            @Override
            public void handle(Buffer buffer) {
                if (size == -1) {
                    //读取消息体长度
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    resultbuffer.appendBuffer(buffer);
                } else {
                    //写入头消息
                    resultbuffer.appendBuffer(buffer);
                    //已经拼接位完整buffer,执行处理
                    handler.handle(resultbuffer);
                    //重置一轮
                    parser.fixedSizeMode(ProtocolConstant.Message_header_length);
                    size=-1;
                    resultbuffer = Buffer.buffer();
                }
            }
        });
        return parser;
    }
}
