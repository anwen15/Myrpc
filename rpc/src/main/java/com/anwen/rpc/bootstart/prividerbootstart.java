package com.anwen.rpc.bootstart;

import com.anwen.RpcApplication;
import com.anwen.rpc.config.RegistryConfig;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.model.ServiceMetaInfo;
import com.anwen.rpc.model.ServiceRegisterInfo;
import com.anwen.rpc.registry.LocalRegistry;
import com.anwen.rpc.registry.Registry;
import com.anwen.rpc.registry.RegistryFactory;
import com.anwen.rpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 25/4/2024 下午9:31
 * 服务提供者初始化
 */
public class prividerbootstart {



    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfos) {
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        //注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfos) {
            String servicename = serviceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(servicename, serviceRegisterInfo.getImplclass());
            //注册到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry =  RegistryFactory.getinstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServicename(servicename);
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServiceaddress(rpcConfig.getHost()+":"+rpcConfig.getPort());
            try {
                registry.registry(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("服务创建失败");
            }
        }

        //启动服务
        VertxTcpServer httpServer = new VertxTcpServer();
        httpServer.start(rpcConfig.getPort());
    }
}
