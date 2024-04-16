package com.anwen.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.anwen.RpcApplication;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.constant.RpcConstant;
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
import java.util.List;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 30/3/2024 下午8:34
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
        ServiceMetaInfo metaInfo = serviceMetaInfoList.get(0);
        // 发送请求
        RpcResponse rpcResponse = VertxTcpClient.dorequest(rpcRequest, serviceMetaInfo);
        return rpcResponse.getData();

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
