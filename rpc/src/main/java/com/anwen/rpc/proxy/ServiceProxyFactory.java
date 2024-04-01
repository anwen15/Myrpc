package com.anwen.rpc.proxy;

import com.anwen.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 30/3/2024 下午8:38
 */
public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getmockproxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    public static <T> T getmockproxy(Class<T> mockclass) {
        return (T) Proxy.newProxyInstance(
                mockclass.getClassLoader(),
                new Class[]{mockclass},
                new MockServiceProxy());
    }
}
