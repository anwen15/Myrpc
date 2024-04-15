package com.anwen.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 14/4/2024 下午6:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;

    /**
     * 协议消息头
     */
    @Data
    public static class Header{
        /**
         * 魔数
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 消息类型
         */
        private byte type;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求id
         */
        private long requestid;

        /**
         * 消息体长度
         */
        private int bodylength;

    }
}
