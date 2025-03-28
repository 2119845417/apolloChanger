package com.ncf.apollodemo.service;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.pojo.entity.AddXxlJob;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ApolloService {

    List<OpenEnvClusterDTO> getEnvclusters(String server,String appId,ApolloOpenApiClient client);

    OpenItemDTO createItem(String env, OpenItemDTO itemDTO,String appId,ApolloOpenApiClient client);

    void createOrUpdateItem(String env, OpenItemDTO itemDTO,String appId,ApolloOpenApiClient client);

    void removeItem(String env, String key,String appId,ApolloOpenApiClient client);

    OpenNamespaceDTO getNamespace(String env,String appId,ApolloOpenApiClient client);

    OpenItemDTO getItem(String env,String key,String appId,ApolloOpenApiClient client);

    OpenReleaseDTO publishNamespace(String env,String appId,ApolloOpenApiClient client);

    ApolloOpenApiClient getClient(String appId);

    List<OpenItemDTO> getItemsByNamespace(String appId, String env,int page, int size,ApolloOpenApiClient client);

    Integer setTask(AddXxlJob addXxlJob);
}
