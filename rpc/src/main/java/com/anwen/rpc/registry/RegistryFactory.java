package com.anwen.rpc.registry;

import com.anwen.rpc.spi.SpiLoader;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 5/4/2024 下午9:36
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();


    /**
     * 获取注册中心
     * @param keys
     * @return
     */
    public static Registry getinstance(String keys) {
        return SpiLoader.getInstance(Registry.class,keys);

    }
}
