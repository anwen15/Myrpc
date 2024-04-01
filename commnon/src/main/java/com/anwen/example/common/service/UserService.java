package com.anwen.example.common.service;

import com.anwen.example.common.model.User;

public interface UserService {
    User getUser(User user);

    default short getnumber() {
        return 1;
    }
}
