package com.anwen.rpc.falut.tolerant;

import com.anwen.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 21/4/2024 下午2:28
 */
public class FailFastTolerantStrategy implements tolerantStrategy{
    @Override
    public RpcResponse dotolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务错误", e);
    }
}
