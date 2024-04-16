package com.anwen.rpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 1/4/2024 下午11:22
 * 服务元信息(注册)
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名
     */
    private String servicename;

    /**
     * 服务版本号
     */
    private String serviceversion = "1.0";

    /**
     * 服务域名
     */
    private String serviceHost="localhost";

    /**
     * 服务端口
     */
    private Integer servicePort=8081;

    /**
     *服务地址
     */
    private String serviceaddress;

    /**
     * todo服务分组
     */
    private String servicegroup = "default";

    /**
     * 获取服务键名
     * @return
     */
    public String getServicekey() {
        return String.format("%s:%s", servicename, serviceversion);
    }

    /**
     * 获取服务注册节点键名
     * @return
     */
    public String getservicenodekey() {
        return String.format("%s/%s", getServicekey(), serviceaddress);
    }

    public String getServiceaddress() {
        if (StrUtil.contains(serviceHost, "http")) {
            return String.format("http://%s%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }
}

