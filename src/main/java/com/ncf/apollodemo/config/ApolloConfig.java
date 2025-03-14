package com.ncf.apollodemo.config;


import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
 
/**
 * @description: apollo openapi连接
 * @author: niucanfei
 */
@Configuration
//@Profile({"dev","test"}) //运行环境
public class ApolloConfig {
    /**
     * apollo portal url
     * apollo 访问地址
     **/
    @Value("${appkey.apollo.url}")
    private String portalUrl;
    /**
     * token apollo中添加的openapi token
     **/
    @Value("${appkey.apollo.token}")
    private String token;
 
    @Bean
    public ApolloOpenApiClient apolloOpenApiClient() {
        ApolloOpenApiClient client = ApolloOpenApiClient.newBuilder()
                .withPortalUrl(portalUrl)
                .withToken(token)
                .build();
        return client;
    }
}