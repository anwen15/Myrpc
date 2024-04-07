package com.anwen.rpc.registry;

import com.anwen.rpc.config.RegistryConfig;
import com.anwen.rpc.model.ServiceMetaInfo;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 注册中心
 */
public interface Registry {
    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void registry(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务(客户端)
     *
     * @param serviceMetaInfo
     */
    void unregistry(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     * @param servicekey
     * @return
     */
    List<ServiceMetaInfo> servicediscovery(String servicekey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartbeat();


}
