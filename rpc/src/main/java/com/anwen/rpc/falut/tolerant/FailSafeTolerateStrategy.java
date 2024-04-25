package com.anwen.rpc.falut.tolerant;

import com.anwen.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 21/4/2024 下午2:29
 */
@Slf4j
public class FailSafeTolerateStrategy implements tolerantStrategy{
    @Override
    public RpcResponse dotolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理", e);
        return new RpcResponse();
    }
}
