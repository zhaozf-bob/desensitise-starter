package com.example.desensitise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesensitiseConfig {

    /**
     * key，使用方法全限定名称或者url
     */
    private String key;

    /**
     * 定位字段路径，data.name
     */
    private String fieldPath;

    /**
     * 脱敏类型
     */
    private String handleType;
}
