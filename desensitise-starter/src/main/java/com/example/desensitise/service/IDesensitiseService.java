package com.example.desensitise.service;

import java.util.List;

/**
 * 脱敏服务实现类
 */
public interface IDesensitiseService {

    /**
     * 脱敏
     *
     * @param keys
     * @param obj
     * @return
     */
    default Object desensitise(List<String> keys, Object obj) {
        return null;
    };
}
