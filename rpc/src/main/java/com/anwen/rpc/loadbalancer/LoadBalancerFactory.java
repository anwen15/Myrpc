package com.anwen.rpc.loadbalancer;

import com.anwen.rpc.spi.SpiLoader;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 19/4/2024 下午9:55
 * 负载均衡器工厂
 */
public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }
    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer defaultloadbalancer = new RoundLoadbalancer();


    /**
     * 获取负载均衡器
     * @param keys
     * @return
     */
    public static LoadBalancer getinstance(String keys) {
        return SpiLoader.getInstance(LoadBalancer.class,keys);

    }
}
