package com.anwen.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 上午12:30
 * 工具类
 */

public class ConfigUtils {

    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadconfig(Class<T> tClass, String prefix) {
        return loadconfig(tClass, prefix, "");
    }

    public static <T> T loadconfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder stringBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            stringBuilder.append("-").append(environment);
        }
        stringBuilder.append(".properties");
        Props props = new Props(stringBuilder.toString());
        return props.toBean(tClass, prefix);
    }

}
