package com.anwen.example.provider;

import com.anwen.example.common.service.UserService;
import com.anwen.rpc.registry.LocalRegistry;
import com.anwen.rpc.server.HttpServer;
import com.anwen.rpc.server.VertxHttpServer;

import java.awt.image.PixelInterleavedSampleModel;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午3:18
 * 服务提供
 */
public class ProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        //启动服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.start(8080);


    }
}
