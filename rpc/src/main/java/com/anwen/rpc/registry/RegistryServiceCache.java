package com.anwen.rpc.registry;

import com.anwen.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 7/4/2024 下午11:50
 * 本地缓存
 */
public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> servicecache;

    /**
     * 写缓存
     * @param newcache
     */
    void writecache(List<ServiceMetaInfo> newcache) {
        servicecache = newcache;
    }

    /**
     * 读缓存
     * @return
     */
    List<ServiceMetaInfo> readcache() {
        return servicecache;
    }

    /**
     * 清空缓存
     */
    void clearcache() {
        servicecache=null;
    }

}
