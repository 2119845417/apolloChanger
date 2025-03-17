package com.ncf.apollodemo.service;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ApolloService {

    List<OpenEnvClusterDTO> getEnvclusters(String server,String appId);

    OpenItemDTO createItem(String env, OpenItemDTO itemDTO,String appId);

    void createOrUpdateItem(String env, OpenItemDTO itemDTO,String appId);

    void removeItem(String env, String key,String appId);

    OpenNamespaceDTO getNamespace(String env,String appId);

    OpenItemDTO getItem(String env,String key,String appId);

    OpenReleaseDTO publishNamespace(String env,String appId);

    ApolloOpenApiClient getClient(String appId);

    List<OpenItemDTO> getItemsByNamespace(String appId, String env,int page, int size);
}
