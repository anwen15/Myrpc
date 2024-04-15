package com.anwen.rpc.server.tcp;

import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.anwen.rpc.protocol.ProtocolMessage;
import com.anwen.rpc.protocol.ProtocolMessage.Header;
import com.anwen.rpc.protocol.ProtocolMessageDecode;
import com.anwen.rpc.protocol.ProtocolMessageEncode;
import com.anwen.rpc.protocol.ProtocolMessageTypeEnum;
import com.anwen.rpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 15/4/2024 下午7:05
 * 请求处理
 */
public class tcpserverhander implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket event) {
        tcpbufferhanderwrapper tcpbufferhanderwrapper = new tcpbufferhanderwrapper(buffer -> {

            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecode.decode(buffer);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RpcRequest rpcRequest = protocolMessage.getBody();
            //处理请求
            RpcResponse response = new RpcResponse();
            try {
                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(aClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                response.setData(result);
                response.setDatatype(method.getReturnType());
                response.setMessage("ok");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                     InstantiationException e) {
                throw new RuntimeException(e);
            }
            Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.response.getKey());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, response);
            try {
                Buffer encode = ProtocolMessageEncode.encode(responseProtocolMessage);
                event.write(encode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        event.handler(tcpbufferhanderwrapper);
        //处理连接
        /**
        event.handler(buffer->{
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecode.decode(buffer);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RpcRequest rpcRequest = protocolMessage.getBody();
            //处理请求
            RpcResponse response = new RpcResponse();
            try {
                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(aClass.getDeclaredConstructor().newInstance(), rpcRequest.getArgs());
                response.setData(result);
                response.setDatatype(method.getReturnType());
                response.setMessage("ok");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                     InstantiationException e) {
                throw new RuntimeException(e);
            }
            Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.response.getKey());
            ProtocolMessage<RpcResponse> responseProtocolMessage = new ProtocolMessage<>(header, response);
            try {
                Buffer encode = ProtocolMessageEncode.encode(responseProtocolMessage);
                event.write(encode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
         **/
    }
}
