package com.ncf.apollodemo.config;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApolloClientFactory {
//    @Autowired
//    private ApolloClientsConfig apolloConfig;
//
//    public ApolloOpenApiClient createClient(String appId) {
//        ApolloClientsConfig.ApolloClientConfig config = apolloConfig.getClients().get(appId);
//        if (config == null) {
//            throw new IllegalArgumentException("Invalid appId: " + appId);
//        }
//        return ApolloOpenApiClient.newBuilder()
//                .withPortalUrl(config.getPortalUrl())
//                .withToken(config.getToken())
//                .build();
//    }
}