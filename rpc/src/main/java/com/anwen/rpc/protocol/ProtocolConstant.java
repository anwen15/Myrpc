package com.anwen.rpc.protocol;

public interface ProtocolConstant {
    /**
     * 消息头长度
     */
    int Message_header_length=17;

    /**
     * 协议魔数
     */
    byte Protocol_magic=0x1;

    /**
     * 协议版本号
     */
    byte Protocol_version=0x1;
}
