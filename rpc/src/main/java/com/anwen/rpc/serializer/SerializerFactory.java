package com.anwen.rpc.serializer;

import com.anwen.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 下午8:34
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer default_serializer=new JdkSerializer();

    /**
     * 获取序列化器
     * @param keys
     * @return
     */
    public static Serializer getinstance(String keys) {

        return SpiLoader.getInstance(Serializer.class, keys);
    }


}
