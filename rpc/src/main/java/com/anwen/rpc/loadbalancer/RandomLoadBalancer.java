package com.anwen.rpc.loadbalancer;

import com.anwen.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 19/4/2024 下午9:36
 * 随机负载均衡器
 */
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestparms, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
