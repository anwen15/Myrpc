package com.anwen.rpc.server;

import io.vertx.core.Vertx;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午3:30
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void start(int port) {
        //vertx实例
        Vertx vertx = Vertx.vertx();
        //创建http服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(request->{

            System.out.println("接受request"+request.method()+""+request.uri());
            //发送http响应
            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("响应");
        });

        httpServer.listen(port,result->{
            if (result.succeeded()) {
                System.out.println("正在监听" + port);
            } else {
                System.out.println("启动服务器失败"+result.cause());
            }
        });
    }
}
