package com.anwen.rpc.loadbalancer;

import com.anwen.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 19/4/2024 下午9:19
 * 轮询负载均衡
 */
public class RoundLoadbalancer implements LoadBalancer {
    /**
     * 当前轮询下标
     */
    private final AtomicInteger currentindex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestparms, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        //如果只有一个服务,无需轮询
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        int inex = currentindex.getAndIncrement() % size;
        return serviceMetaInfoList.get(inex);
    }
}
