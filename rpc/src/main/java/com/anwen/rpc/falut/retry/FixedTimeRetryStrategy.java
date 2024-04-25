package com.anwen.rpc.falut.retry;

import com.anwen.rpc.model.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 20/4/2024 下午2:26
 * 固定时间间隔重试策略
 */
@Slf4j
public class FixedTimeRetryStrategy implements RetryStrategy{
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doretry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryerBuilder = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3l, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数{}", attempt.getAttemptNumber());
                    }
                }).build();
        return retryerBuilder.call(callable);

    }
}
