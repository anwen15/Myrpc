package com.anwen.rpc.registry;

import cn.hutool.json.JSONUtil;
import com.anwen.rpc.config.RegistryConfig;
import com.anwen.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 5/4/2024 下午8:35
 */
public class EtcdRegistry implements Registry {
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
    }

    @Override
    public void unregistry(ServiceMetaInfo serviceMetaInfo) {
        kvclient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getservicenodekey(), StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> servicediscovery(String servicekey) {
        String searchPrefix = ETCD_ROOT_PATH + servicekey + "/";
        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvclient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            return keyValues.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("获取服务列表失败");
        }
    }

    @Override
    public void destroy() {
        System.out.println("该节点下线");
        if (kvclient != null) {
            kvclient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
