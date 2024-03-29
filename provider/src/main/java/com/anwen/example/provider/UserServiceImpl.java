package com.anwen.example.provider;

import com.anwen.example.common.model.User;
import com.anwen.example.common.service.UserService;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午3:16
 */
public class UserServiceImpl implements UserService {


    public User getUser(User user) {
        System.out.println("用户名"+user.getName());
        return user;
    }
}
