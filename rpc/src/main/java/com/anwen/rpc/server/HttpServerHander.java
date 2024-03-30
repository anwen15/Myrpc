package com.anwen.rpc.server;

import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.registry.LocalRegistry;
import com.anwen.rpc.serializer.JdkSerializer;
import com.anwen.rpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午10:58
 * 请求处理器
 */
public class HttpServerHander implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer = new JdkSerializer();
        System.out.println("接受request"+request.method()+""+request.uri());

        //异步处理http
        request.bodyHandler(body->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest=null;
            try {
                rpcRequest=serializer.deserialize(bytes,RpcRequest.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RpcResponse response = new RpcResponse();
            if (request == null) {
                response.setMessage("无请求");
                doresponse(request,response,serializer);
                return;
            }
            try {
                //反射调用
                Class<?> implclass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implclass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object invoke = method.invoke(implclass.newInstance(), rpcRequest.getArgs());
                response.setData(invoke);
                response.setDatatype(method.getReturnType());
                response.setMessage("OK");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                     InstantiationException e) {
                throw new RuntimeException(e);
            }
            doresponse(request,response,serializer);
        });
    }

    /**
     * 响应
     */
    void doresponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse response = request.response()
                .putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            response.end(Buffer.buffer());
        }
    }
}
