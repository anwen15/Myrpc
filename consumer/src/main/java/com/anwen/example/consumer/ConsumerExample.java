package com.anwen.example.consumer;

import com.anwen.example.common.model.User;
import com.anwen.example.common.service.UserService;
import com.anwen.rpc.config.RpcConfig;
import com.anwen.rpc.proxy.ServiceProxyFactory;
import com.anwen.rpc.utils.ConfigUtils;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午3:23
 * 消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadconfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
        UserService userService =ServiceProxyFactory.getProxy(UserService.class);
        User user=new User();
        user.setName("安稳");
        User newuser = userService.getUser(user);
        if (newuser != null) {
            System.out.println(newuser.getName());
        } else {
            System.out.println("失败");
        }
    }

}
