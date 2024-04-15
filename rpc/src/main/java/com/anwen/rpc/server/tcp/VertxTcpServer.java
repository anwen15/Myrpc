package com.anwen.rpc.server.tcp;

import com.anwen.rpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 14/4/2024 下午11:35
 */
public class VertxTcpServer implements HttpServer {

    private byte[] handlerequest(byte[] requestdata) {
        return "hello".getBytes();
    }
    @Override
    public void start(int port) {
        //vertx实例
        Vertx vertx = Vertx.vertx();
        //创建tcp服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(new tcpserverhander());

        server.listen(port,result->{
            if (result.succeeded()) {
                System.out.println("TCP服务器启动");

            }else {
                System.out.println("tcp服务器启动失败");

            }
        });

    }

    public static void main(String[] args) {
        new VertxTcpServer().start(8888);
    }
}
