package com.anwen.rpc.protocol;

import lombok.Getter;

/**
 * 协议消息状态枚举
 */
@Getter
public enum ProtocolMessagestatusEnum {
    ok("ok", 20),
    bad_request("badrequest",40),
    bad_Response("badreponse",50);

    private final String text;

    private final int value;

    ProtocolMessagestatusEnum(String text, int value) {
        this.text = text;
        this.value = value;

    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessagestatusEnum getEnum(int value) {
        for (ProtocolMessagestatusEnum protocolMessagestatusEnum : ProtocolMessagestatusEnum.values()) {
            if (protocolMessagestatusEnum.value == value) {
                return protocolMessagestatusEnum;
            }
        }
        return null;
    }
}
