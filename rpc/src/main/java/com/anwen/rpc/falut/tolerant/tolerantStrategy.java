package com.anwen.rpc.falut.tolerant;

import com.anwen.rpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错机制
 */
public interface tolerantStrategy {
    /**
     * 容错
     * @param context
     * @param e
     * @return
     */
    RpcResponse dotolerant(Map<String, Object> context, Exception e);
}
