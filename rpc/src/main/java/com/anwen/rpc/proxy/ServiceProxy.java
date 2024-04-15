package com.anwen.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.anwen.RpcApplication;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.constant.RpcConstant;
import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.model.ServiceMetaInfo;
import com.anwen.rpc.protocol.*;
import com.anwen.rpc.protocol.ProtocolMessage.Header;
import com.anwen.rpc.registry.Registry;
import com.anwen.rpc.registry.RegistryFactory;
import com.anwen.rpc.serializer.Serializer;
import com.anwen.rpc.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
        netClient.connect(metaInfo.getServicePort(), metaInfo.getServiceHost(), result -> {
            if (result.succeeded()) {
                System.out.println("连接tcp服务器成功");
                NetSocket netSocket = result.result();
                //发送消息
                Header header = new Header();
                header.setMagic(ProtocolConstant.Protocol_magic);
                header.setVersion(ProtocolConstant.Protocol_version);
                header.setSerializer((byte) ProtocolMessageSerializerEnum.getenumbyvalue(rpcConfig.getSerializer()).getKey());
                header.setType((byte) ProtocolMessageTypeEnum.request.getKey());
                header.setRequestid(IdUtil.getSnowflakeNextId());
                ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>(header, rpcRequest);
                //编码
                try {
                    Buffer encode = ProtocolMessageEncode.encode(protocolMessage);
                    netSocket.write(encode);
                } catch (IOException e) {
                    throw new RuntimeException("协议消息编码错误");
                }
                //接受响应
                netSocket.handler(buffer -> {
                    try {
                        ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecode.decode(buffer);
                        completableFuture.complete(decode.getBody());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                System.out.println("连接tcp服务器失败");
            }
        });
        RpcResponse response = completableFuture.get();
        //关闭连接
        netClient.close();
        return response.getData();
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
