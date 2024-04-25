package com.anwen.rpc.falut.retry;

import com.anwen.rpc.spi.SpiLoader;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 20/4/2024 下午2:47
 */
public class RetryStrategyFactory {
    static {
        SpiLoader.load(RetryStrategy.class);
    }
    /**
     * 默认
     */
    private static final RetryStrategy defaultretrystrategy = new NoretryStrstegy();


    /**
     * 获取
     * @param keys
     * @return
     */
    public static RetryStrategy getinstance(String keys) {
        return SpiLoader.getInstance(RetryStrategy.class,keys);

    }
}
