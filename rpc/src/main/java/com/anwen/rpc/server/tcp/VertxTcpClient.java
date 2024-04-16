package com.anwen.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.anwen.RpcApplication;
import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.model.ServiceMetaInfo;
import com.anwen.rpc.protocol.*;
import com.anwen.rpc.protocol.ProtocolMessage.Header;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 上午11:56
 * tcp请求服务器
 */
public class VertxTcpClient {

    public static RpcResponse dorequest(RpcRequest rpcRequest, ServiceMetaInfo  ServiceMetaInfo) throws ExecutionException, InterruptedException {
        //发送tcp请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
        netClient.connect(ServiceMetaInfo.getServicePort(), ServiceMetaInfo.getServiceHost(),result->{
            if (!result.succeeded()) {
                System.out.println("连接tcp服务器失败");
                return;
            }
            NetSocket netSocket = result.result();
            //发送消息
            Header header = new Header();
            header.setMagic(ProtocolConstant.Protocol_magic);
            header.setVersion(ProtocolConstant.Protocol_version);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getenumbyvalue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.request.getKey());
            header.setRequestid(IdUtil.getSnowflakeNextId());
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>(header, rpcRequest);
            //编码
            try {
                Buffer encode = ProtocolMessageEncode.encode(protocolMessage);
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }
            //接受响应
            tcpbufferhanderwrapper tcpbufferhanderwrapper = new tcpbufferhanderwrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecode.decode(buffer);
                    completableFuture.complete(decode.getBody());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            netSocket.handler(tcpbufferhanderwrapper);

        });
        RpcResponse rpcResponse = completableFuture.get();
        netClient.close();
        return rpcResponse;
    }


}
