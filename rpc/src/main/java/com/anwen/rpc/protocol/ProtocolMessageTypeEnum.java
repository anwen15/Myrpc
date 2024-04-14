package com.anwen.rpc.protocol;

import lombok.Getter;

/**
 * 协议消息类型
 */
@Getter
public enum ProtocolMessageTypeEnum {
    request(0),
    response(1),
    header_beat(2),
    others(3);

    private final int key;


    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    public static ProtocolMessageTypeEnum getenum(int key) {
        for (ProtocolMessageTypeEnum value : ProtocolMessageTypeEnum.values()) {
            if (value.key == key) {
                return value;
            }
        }
        return null;
    }
}
