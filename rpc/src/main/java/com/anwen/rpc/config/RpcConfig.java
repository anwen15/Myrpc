package com.anwen.rpc.config;

import com.anwen.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 上午12:13
 * 配置文件
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "MyRPC";

    /**
     * version
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String host = "localhost";

    /**
     * 服务器端口号
     */
    private Integer port = 8081;

    /**
     * 模拟调用
     */
    private boolean mock=false;

    /**
     * 序列化器
     */
    private String serializer= SerializerKeys.JDK;

    /**
     * 注册中心
     */
    private RegistryConfig registryConfig = new RegistryConfig();


}
