package com.anwen.rpc.server.tcp;

import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.model.ServiceMetaInfo;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;

import java.util.concurrent.CompletableFuture;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 上午11:56
 * tcp请求服务器
 */
public class VertxTcpClient {

    public static RpcResponse dorequest(RpcRequest rpcRequest, ServiceMetaInfo  ServiceMetaInfo) {
        //发送tcp请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
        netClient.connect(ServiceMetaInfo.getServicePort(), ServiceMetaInfo.getServiceHost(),result->{

        })
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
