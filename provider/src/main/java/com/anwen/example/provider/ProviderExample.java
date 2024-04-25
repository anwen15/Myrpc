package com.anwen.example.provider;

import com.anwen.example.common.service.UserService;
import com.anwen.rpc.bootstart.prividerbootstart;
import com.anwen.rpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午3:18
 * 服务提供
 */
public class ProviderExample {
    public static void main(String[] args) {
        List<ServiceRegisterInfo<?>> serviceRegisterInfos = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfos.add(serviceRegisterInfo);
        prividerbootstart.init(serviceRegisterInfos);


    }
}
