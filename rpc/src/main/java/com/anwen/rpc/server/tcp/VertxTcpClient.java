package com.anwen.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 上午11:56
 */
public class VertxTcpClient {

    public void start() {
        //创建vertx实例
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8888,"localhost",result->{
            if (result.succeeded()) {
                System.out.println("连接tcp服务器");
                NetSocket netSocket = result.result();
                //发送数据
                netSocket.write("hello,tcp");
                netSocket.handler(hander -> {
                    System.out.println("接受响应" + hander.toString());
                });
            } else {
                System.out.println("连接失败");
            }
        });

    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
