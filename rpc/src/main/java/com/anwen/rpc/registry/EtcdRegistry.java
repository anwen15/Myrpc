package com.anwen.rpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.anwen.rpc.config.RegistryConfig;
import com.anwen.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 5/4/2024 下午8:35
 */
public class EtcdRegistry implements Registry {
    /**
     * 本机注册节点key集合
     */
    private final Set<String> localRegistryKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();
    private Client client;

    private KV kvclient;

    /**
     * 根路径
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvclient = client.getKVClient();
        heartbeat();
    }

    @Override
    public void registry(ServiceMetaInfo serviceMetaInfo) throws Exception {
        Lease leaseClient = client.getLeaseClient();
        //创建一个30s的租约
        long id = leaseClient.grant(30).get().getID();
        //设置存储键值对
        String registrykey = ETCD_ROOT_PATH + serviceMetaInfo.getservicenodekey();
        ByteSequence key = ByteSequence.from(registrykey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        PutOption putOption = PutOption.builder().withLeaseId(id).build();
        kvclient.put(key, value, putOption);
        localRegistryKeySet.add(registrykey);
    }

    @Override
    public void unregistry(ServiceMetaInfo serviceMetaInfo) {
        kvclient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getservicenodekey(), StandardCharsets.UTF_8));
        localRegistryKeySet.remove(ETCD_ROOT_PATH + serviceMetaInfo.getservicenodekey());
    }

    @Override
    public List<ServiceMetaInfo> servicediscovery(String servicekey) {
        //从缓存中查找
        List<ServiceMetaInfo> cacheServiceMetaInfoList = registryServiceCache.readcache();
        if (cacheServiceMetaInfoList != null) {
            return cacheServiceMetaInfoList;
        }
        //前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + servicekey + "/";
        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvclient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            List<ServiceMetaInfo> serviceMetaInfos = keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
            registryServiceCache.writecache(serviceMetaInfos);
            return serviceMetaInfos;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("获取服务列表失败");
        }
    }

    @Override
    public void destroy() {
        System.out.println("该节点下线");
        for (String keyset : localRegistryKeySet) {
            try {
                kvclient.delete(ByteSequence.from(keyset, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(keyset + "删除失败");
            }

        }
        if (kvclient != null) {
            kvclient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartbeat() {
        CronUtil.schedule("*/10 * * * * *",new Task(){

            @Override
            public void execute() {
                //遍历本节点所有的key
                for (String key : localRegistryKeySet) {
                    try {
                        List<KeyValue> keyValues = kvclient.get(
                                        ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        //该节点已过期
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        //节点未过期
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        registry(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key+"续签失败");
                    }
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
