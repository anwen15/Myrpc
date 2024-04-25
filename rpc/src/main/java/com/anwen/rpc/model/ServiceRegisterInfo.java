package com.anwen.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 25/4/2024 下午9:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {

    /**
     * 服务名称
     */
    public String serviceName;

    /**
     * 实现类
     */
    private Class<? extends T> implclass;
}
