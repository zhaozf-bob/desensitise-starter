package com.example.desensitise.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 脱敏自动配置类
 */
@Configuration
@EnableConfigurationProperties(DesensitiseProperties.class)
@ComponentScan(basePackages = "com.example.desensitise")
public class DesensitiseAutoConfiguration {
}
