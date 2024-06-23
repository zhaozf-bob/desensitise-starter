package com.example.desensitise.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gzsendi.desensities")
@Data
public class DesensitiseProperties {

    /**
     * 是否启用脱敏
     */
    private Boolean enabled = false;

    private Long bufferTimes = 1000 * 60 * 5l;

}
