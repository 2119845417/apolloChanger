package com.ncf.apollodemo.service.impl;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.service.ApolloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ApolloServiceImpl implements ApolloService {
    //apollo中项目id
    @Value("${appkey.apollo.appId}")
    private String appId;
    //apollo操作用户
    @Value("${appkey.apollo.opUser}")
    private String opUser;
    //apollo中集群名称，apollo默认集群为default
    @Value("${appkey.apollo.cluster}")
    private String cluster;
    //apollo中集群内namespace名称
    @Value("${appkey.apollo.namespace}")
    private String namespace;

    //apollo操作客户端
    @Autowired
    private ApolloOpenApiClient apolloClient;


    @Override
    public List<OpenEnvClusterDTO> getEnvclusters(String server) {
        List<OpenEnvClusterDTO> envClusterInfo = apolloClient.getEnvClusterInfo(server);
        return envClusterInfo;
    }

    @Override
    public OpenItemDTO createItem(String env,OpenItemDTO itemDTO) {
        itemDTO.setDataChangeCreatedBy(opUser);
        OpenItemDTO item = apolloClient.createItem(appId, env, cluster, namespace, itemDTO);
        return item;
    }

    @Override
    public void createOrUpdateItem(String env, OpenItemDTO itemDTO) {
        itemDTO.setDataChangeCreatedBy(opUser);
        apolloClient.createOrUpdateItem(appId, env, cluster, namespace, itemDTO);
    }

    @Override
    public void removeItem(String env, String key) {
        apolloClient.removeItem(appId, env, cluster, namespace, key, opUser);
    }

    @Override
    public OpenNamespaceDTO getNamespace(String env) {
        OpenNamespaceDTO openNamespaceDTO = apolloClient.getNamespace(appId, env, cluster, namespace);
        return openNamespaceDTO;
    }

    @Override
    public OpenItemDTO getItem(String env, String key) {
        OpenItemDTO dto = apolloClient.getItem(appId, env, cluster, namespace, key);
        return dto;
    }

    @Override
    public OpenReleaseDTO publishNamespace(String env) {
        NamespaceGrayDelReleaseDTO namespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
        //配置版本名称
        namespaceGrayDelReleaseDTO.setReleaseTitle(System.currentTimeMillis() + "-release");
        //刷新说明
        namespaceGrayDelReleaseDTO.setReleaseComment("auto release");
        namespaceGrayDelReleaseDTO.setReleasedBy(opUser);
        OpenReleaseDTO openReleaseDTO = apolloClient.publishNamespace(appId, env, cluster, namespace, namespaceGrayDelReleaseDTO);
        return openReleaseDTO;
    }
}
