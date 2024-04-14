package com.anwen.example.provider;

import com.anwen.RpcApplication;
import com.anwen.example.common.service.UserService;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.model.ServiceMetaInfo;
import com.anwen.rpc.registry.LocalRegistry;
import com.anwen.rpc.registry.Registry;
import com.anwen.rpc.registry.RegistryFactory;
import com.anwen.rpc.server.HttpServer;
import com.anwen.rpc.server.VertxHttpServer;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午3:18
 * 服务提供
 */
public class ProviderExample {
    public static void main(String[] args) {
        RpcApplication.init();
        //注册服务
        String servicename = UserService.class.getName();
        LocalRegistry.register(servicename, UserServiceImpl.class);
        //注册到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getinstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServicename(servicename);
        serviceMetaInfo.setServicePort(rpcConfig.getPort());
        serviceMetaInfo.setServiceHost(rpcConfig.getHost());
        serviceMetaInfo.setServiceaddress(rpcConfig.getHost()+":"+rpcConfig.getPort());
        try {
            registry.registry(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        //启动服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.start(rpcConfig.getPort());


    }
}
