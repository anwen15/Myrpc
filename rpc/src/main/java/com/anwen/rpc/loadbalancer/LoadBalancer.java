package com.anwen.rpc.loadbalancer;

import com.anwen.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 19/4/2024 下午9:16
 * 负载均衡器(消费端使用)
 */
public interface LoadBalancer {
    /**
     * 选择服务调用
     * @param requestparms
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestparms, List<ServiceMetaInfo> serviceMetaInfoList);
}
