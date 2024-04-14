package com.anwen.rpc.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化器枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {
    jdk(0, "jdk"),
    json(1, "json"),
    kryo(2,"kryo"),
    hessian(3,"hession")
    ;
    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return
     */
    public static List<String> getvalues() {
        return Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    public static ProtocolMessageSerializerEnum getenum(int key) {
        for (ProtocolMessageSerializerEnum value : ProtocolMessageSerializerEnum.values()) {
            if (value.key == key) {
                return value;
            }
        }
        return null;
    }
}
