package com.ncf.apollodemo.config;

import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.ncf.apollodemo.listener.DingTalkCallbackListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 获取长连接实例
 */
@Configuration
public class DingTalkConfig {

    @Value("${dingtalk.appKey}")
    private String appKey;

    @Value("${dingtalk.appSecret}")
    private String appSecret;

    @Autowired
    private DingTalkCallbackListener dingTalkCallbackListener;

    public void initDingTalkConnection() throws Exception {
        OpenDingTalkStreamClientBuilder
                .custom()
                .credential(new AuthClientCredential(appKey, appSecret))
                .registerCallbackListener("/v1.0/card/instances/callback", dingTalkCallbackListener)
                .build().start();
    }
}