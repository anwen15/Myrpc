package com.anwen.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.anwen.RpcApplication;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.constant.RpcConstant;
import com.anwen.rpc.falut.retry.RetryStrategy;
import com.anwen.rpc.falut.retry.RetryStrategyFactory;
import com.anwen.rpc.falut.tolerant.TolerateStrategyFactory;
import com.anwen.rpc.falut.tolerant.tolerantStrategy;
import com.anwen.rpc.loadbalancer.LoadBalancer;
import com.anwen.rpc.loadbalancer.LoadBalancerFactory;
import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.model.ServiceMetaInfo;
import com.anwen.rpc.registry.Registry;
import com.anwen.rpc.registry.RegistryFactory;
import com.anwen.rpc.serializer.Serializer;
import com.anwen.rpc.serializer.SerializerFactory;
import com.anwen.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 30/3/2024 下午8:34
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  {
        // 指定序列化器
        Serializer serializer = SerializerFactory.getinstance(RpcApplication.getRpcConfig().getSerializer());
        String servicename = method.getDeclaringClass().getName();
        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        // 序列化
        //byte[] bodyBytes = serializer.serialize(rpcRequest);
        //从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getinstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServicename(servicename);
        serviceMetaInfo.setServiceversion(RpcConstant.SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.servicediscovery(serviceMetaInfo.getServicekey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }
        //负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getinstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestparms = new HashMap<>();
        requestparms.put("methodname", rpcRequest.getMethodName());
        ServiceMetaInfo metaInfo = loadBalancer.select(requestparms, serviceMetaInfoList);
        // 发送请求
        //重试策略
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getinstance(rpcConfig.getRetrystrategy());
            RpcResponse rpcResponse = retryStrategy.doretry(() -> VertxTcpClient.dorequest(rpcRequest, metaInfo));
            return rpcResponse.getData();
        } catch (Exception e) {
            //容错
            tolerantStrategy tolerantStrategy = TolerateStrategyFactory.getinstance(rpcConfig.getTolerantStrategy());
            RpcResponse response = tolerantStrategy.dotolerant(null, e);
            return response.getData();
        }



    }
//try (HttpResponse httpResponse = HttpRequest.post(metaInfo.getServiceaddress())
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;

}
