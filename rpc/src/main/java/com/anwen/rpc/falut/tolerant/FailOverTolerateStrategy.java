package com.anwen.rpc.falut.tolerant;

import com.anwen.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 21/4/2024 下午2:32
 * 转移到其他服务
 */
public class FailOverTolerateStrategy implements tolerantStrategy{
    @Override
    public RpcResponse dotolerant(Map<String, Object> context, Exception e) {
        return null;

    }
}
