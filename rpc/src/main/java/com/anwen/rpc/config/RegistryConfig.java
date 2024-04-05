package com.anwen.rpc.config;

import lombok.Data;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 5/4/2024 下午8:25
 */
@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间
     */
    private Long timeout=10000L;

}
