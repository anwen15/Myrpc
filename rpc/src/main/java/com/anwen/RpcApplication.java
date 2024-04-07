package com.anwen;

import com.anwen.rpc.config.RegistryConfig;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.constant.RpcConstant;
import com.anwen.rpc.registry.Registry;
import com.anwen.rpc.registry.RegistryFactory;
import com.anwen.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 上午12:40
 * 框架获取,单例实现
 */
@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化框架,传入自定义配置
     * @param newrpcConfig
     */

    public static void init(RpcConfig newrpcConfig) {
        rpcConfig=newrpcConfig;
        log.info("init,rpcconfig={}",newrpcConfig.toString());
        //注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getinstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("注册中心初始化",registryConfig);
        //创建并注册shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    public static void init() {
        RpcConfig newRpcconfig;
        try {
            newRpcconfig = ConfigUtils.loadconfig(RpcConfig.class, RpcConstant.CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcconfig=new RpcConfig();
        }
        init(newRpcconfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig==null) {
            synchronized (RpcApplication.class){
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;

    }


}
