package com.example.desensitise.service;

import java.util.List;

/**
 * 脱敏服务实现类
 */
@FunctionalInterface
public interface IDesensitiseService {

    /**
     * 脱敏
     *
     * @param keys
     * @param obj
     * @return
     */
    Object desensitise(List<String> keys, Object obj);
}
