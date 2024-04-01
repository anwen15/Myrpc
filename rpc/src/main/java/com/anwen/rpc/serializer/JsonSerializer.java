package com.anwen.rpc.serializer;



import com.anwen.rpc.model.RpcRequest;
import com.anwen.rpc.model.RpcResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 下午4:26
 */
public class JsonSerializer implements Serializer {
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T readValue = OBJECT_MAPPER.readValue(bytes, type);
        if (readValue instanceof RpcRequest) {
            return handleResquest((RpcRequest) readValue, type);
        }
        if (readValue instanceof RpcResponse) {
            return handleResponse((RpcResponse) readValue, type);
        }
        return readValue;
    }

    /**
     *
     * @param rpcRequest
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleResquest(RpcRequest rpcRequest, Class<T> type) throws IOException{
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();
        //处理每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> aClass = parameterTypes[i];
            //类型不同重新处理
            if (!aClass.isAssignableFrom(args[i].getClass())) {
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(bytes, aClass);
            }
        }
        return type.cast(rpcRequest);
    }

    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(bytes, rpcResponse.getDatatype()));
        return type.cast(rpcResponse);
    }
}
