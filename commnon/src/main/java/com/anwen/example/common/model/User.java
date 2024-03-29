package com.anwen.example.common.model;

import java.io.Serializable;

/**
 * @author nicefang
 * @version 1.0
 * @description: TODO
 * @date 29/3/2024 下午2:59
 */
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
