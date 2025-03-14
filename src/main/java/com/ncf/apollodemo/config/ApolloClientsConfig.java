//package com.ncf.apollodemo.config;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import java.util.Map;
//
//@ConfigurationProperties(prefix = "appkey.apollo")
//public class ApolloClientsConfig {
//    // Key 为 appId，Value 包含 portalUrl 和 token
//    private Map<String, ApolloClientConfig> clients;
//
//
//    @Data
//    public static class ApolloClientConfig {
//        private String portalUrl;
//        private String token;
//    }
//}