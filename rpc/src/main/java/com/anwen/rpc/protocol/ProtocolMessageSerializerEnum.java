package com.anwen.rpc.protocol;

import cn.hutool.core.util.ObjectUtil;
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
    hessian(3,"hessian")
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

    /**
     * 根据key获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getenum(int key) {
        for (ProtocolMessageSerializerEnum value : ProtocolMessageSerializerEnum.values()) {
            if (value.key == key) {
                return value;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessageSerializerEnum getenumbyvalue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()) {
            if (protocolMessageSerializerEnum.value .equals(value)) {
                return protocolMessageSerializerEnum;
            }
        }
        return null;

    }
}
