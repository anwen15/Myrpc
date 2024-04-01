package com.anwen.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 31/3/2024 下午5:18
 */
public class KryoSerializer implements Serializer {

    /**
     * 线程不安全,需要用threadlocal
     *
     *
     */
    private static  final ThreadLocal<Kryo> KRYO_THREAD_LOCAL=ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        //设置动态序列化和反序列化类,不提前注册所有类
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        KRYO_THREAD_LOCAL.get().writeObject(output, object);
        output.close();
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(inputStream);
        T readObject = KRYO_THREAD_LOCAL.get().readObject(input, type);
        input.close();
        return readObject;
    }
}
