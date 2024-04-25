package com.anwen.rpc.falut.tolerant;

import com.anwen.rpc.spi.SpiLoader;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 21/4/2024 下午2:53
 */
public class TolerateStrategyFactory {
    static {
        SpiLoader.load(tolerantStrategy.class);
    }

    /**
     * 默认
     */
    private static final tolerantStrategy tolerantStrategy = new FailFastTolerantStrategy();


    /**
     * 获取
     * @param keys
     * @return
     */
    public static tolerantStrategy getinstance(String keys) {
        return SpiLoader.getInstance(tolerantStrategy.class,keys);

    }
}
