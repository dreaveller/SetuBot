package com.ecc.setubot.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommonConfig {
    private Map<String, String> configMap = new HashMap<>();

    public String get(String key) {
        return configMap.get(key);
    }

    public void set(String key, String value) {
        configMap.put(key, value);
    }

    public Long getBotAccount() {
        return Long.valueOf(configMap.get("account"));
    }

    public String getBotPassword() {
        return configMap.get("password");
    }

    public Long getAdmin() {
        return Long.valueOf(configMap.get("admin"));
    }
}
