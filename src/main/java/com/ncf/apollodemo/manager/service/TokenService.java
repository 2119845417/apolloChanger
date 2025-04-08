package com.ncf.apollodemo.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.dao.ApolloClientConfigDao;
import com.ncf.apollodemo.pojo.domain.ApolloClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    @Autowired
    private ApolloClientConfigDao apolloClientConfigDao;
//    // 模拟从数据库/配置中心读取token
//    private Map<String, String> tokenMap = new ConcurrentHashMap<>();
//
//    @PostConstruct
//    public void init() {
//        tokenMap.put("101", "e3d7d9c4c6f97bfab41361dfa67b8ca855d13b6207ada14f7f596b3f4a8eaa3a");
////        e905c91c37bcdbefc74afeab2ba27246f0d1398534b170d3d9a0017ea5588a57在家
////        5327be5f5f0603cb432d6c9ee53aa8ec7622a071a62a691a0885ec0d0bea17ea
//        tokenMap.put("102", "e879f580c7a3a71a22cf272277afb94948504a820808dcb7b8ac7ba317b837a6");
//    }

    public String getTokenByAppId(String appId) {
        QueryWrapper<ApolloClientConfig> apolloClientConfigQueryWrapper = new QueryWrapper<>();
        apolloClientConfigQueryWrapper.eq("app_id",appId);
        ApolloClientConfig apolloClientConfig = apolloClientConfigDao.selectOne(apolloClientConfigQueryWrapper);
        if(apolloClientConfig != null){
            return apolloClientConfig.getToken();
        }else {
            throw new RuntimeException("未找到appid对应的Token3");
        }
    }
}