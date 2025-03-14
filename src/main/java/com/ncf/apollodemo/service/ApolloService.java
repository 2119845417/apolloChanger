package com.ncf.apollodemo.service;

import com.ctrip.framework.apollo.openapi.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ApolloService {

    List<OpenEnvClusterDTO> getEnvclusters(String server);

    OpenItemDTO createItem(String env, OpenItemDTO itemDTO);

    void createOrUpdateItem(String env, OpenItemDTO itemDTO);

    void removeItem(String env, String key);

    OpenNamespaceDTO getNamespace(String env);

    OpenItemDTO getItem(String env,String key);

    OpenReleaseDTO publishNamespace(String env);

    List<OpenItemDTO> getOwnerByAppId(Integer appId);
}
