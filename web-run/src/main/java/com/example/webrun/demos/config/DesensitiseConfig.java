package com.example.webrun.demos.config;

import com.example.desensitise.service.IDesensitiseConfigProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * 脱敏配置
 */
@Configuration
public class DesensitiseConfig {

    @Bean
    public IDesensitiseConfigProvider desensitiseConfigProvider() {
        return keys -> {
            // todo 可以增加缓存
            if (keys.contains("/user")) {
                ArrayList<com.example.desensitise.model.DesensitiseConfig> list = new ArrayList<>();
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "name", "中文名"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "age", "number"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "identifiers", "中文名"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schools.name", "中文名"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schools.user.name", "中文名"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schools.user.age", "number"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schools.users.name", "中文名"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schools.users.age", "number"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schoolMap.name", "中文名"));
                list.add(new com.example.desensitise.model.DesensitiseConfig("/user", "schoolMap2", "中文名"));
                return list;
            }
            return null;
        };
    }
}
