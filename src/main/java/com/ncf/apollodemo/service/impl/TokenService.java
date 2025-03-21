package com.ncf.apollodemo.service.impl;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    // 模拟从数据库/配置中心读取token
    private Map<String, String> tokenMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        tokenMap.put("101", "e905c91c37bcdbefc74afeab2ba27246f0d1398534b170d3d9a0017ea5588a57");
//        e905c91c37bcdbefc74afeab2ba27246f0d1398534b170d3d9a0017ea5588a57在家
//        5327be5f5f0603cb432d6c9ee53aa8ec7622a071a62a691a0885ec0d0bea17ea
        tokenMap.put("102", "a7027aeb7c7ea66eb8c730bf325068192da237381fe94a5ae7ba95c952f2df1a");
    }

    public String getTokenByAppId(String appId) {
        return Optional.ofNullable(tokenMap.get(appId))
            .orElseThrow(() -> new RuntimeException("未找到appid对应的Token"));
    }
}