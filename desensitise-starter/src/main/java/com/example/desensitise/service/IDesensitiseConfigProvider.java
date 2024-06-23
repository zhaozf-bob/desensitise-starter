package com.example.desensitise.service;

import com.example.desensitise.model.DesensitiseConfig;

import java.util.List;

public interface IDesensitiseConfigProvider {

    /**
     * 根据key匹配脱敏配置
     * @param keys
     * @return
     */
    List<DesensitiseConfig> match(List<String> keys);
}
