package com.anwen.rpc.loadbalancer;

import com.anwen.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 19/4/2024 下午9:39
 * 一致性哈希负载均衡器
 */


public class ConsitententHashLoadBalancer implements LoadBalancer{

    /**
     * 一致性hash环
     */
    private final TreeMap<Integer, ServiceMetaInfo> nodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int node_num = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestparms, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        //构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < node_num; i++) {
                int hash = gethash(serviceMetaInfo.getServiceaddress() + "#" + i);
                nodes.put(hash, serviceMetaInfo);
            }
        }
        //请求hash值
        int hash = gethash(requestparms);
        //选择大于等于该hash值的节点
        Entry<Integer, ServiceMetaInfo> serviceMetaInfoEntry = nodes.ceilingEntry(hash);
        if (serviceMetaInfoEntry == null) {
            serviceMetaInfoEntry = nodes.firstEntry();
        }
        return serviceMetaInfoEntry.getValue();

    }

    /**
     * 哈希算法
     * @param key
     * @return
     */
    public int gethash(Object key) {
        return key.hashCode();
    }
}
