package com.anwen.rpc.retry;

import com.anwen.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 20/4/2024 下午2:23
 * 不重试策略
 */
public class NoretryStrstegy implements RetryStrategy {

    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doretry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
