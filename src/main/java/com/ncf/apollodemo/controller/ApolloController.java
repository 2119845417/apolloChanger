package com.ncf.apollodemo.controller;
 

import com.alibaba.fastjson2.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceGrayDelReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenReleaseDTO;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
 
import java.util.Date;
 
/**
 * 告警参数更新
 */
@RestController
@EnableApolloConfig(value = "application")
@RequestMapping(value = "apollo")
@Api(value = "apollo", tags = "告警参数更新")
public class ApolloController {
    private static final Logger logger = LoggerFactory.getLogger(ApolloController.class);
 
    //apollo中项目id
    @Value("${appkey.apollo.appId}")
    private String appId;
 
    //apollo操作用户
    @Value("${appkey.apollo.opuser}")
    private String opUser;
 
    //apollo中集群名称，apollo默认集群为default
    @Value("${appkey.apollo.cluster}")
    private String cluster;
 
    //apollo中集群内namespace名称
    @Value("${appkey.apollo.namespace}")
    private String namespace;
 
    //apollo操作客户端
    private ApolloOpenApiClient apolloClient;
    public ApolloController(ApolloOpenApiClient client) {
        this.apolloClient = client;
    }
 
    /**
     * 获取环境列表
     * @param server apollo中服务id
     * @return [{"clusters":["default"],"env":"DEV"}]
     */
    @GetMapping("/envclusters/{server}")
    @ApiOperation(value = "获取环境列表", notes = "获取环境列表")
    public Object getEnvclusters(@PathVariable String server) {
        return JSON.toJSONString(apolloClient.getEnvClusterInfo(server));
    }
 
    /**
     * 向apollo中新增配置项，为未发布状态。
     * post uri:apollo/dev/add
     * @param env 指定apollo的数据环境
     * @return
     */
    @PostMapping("/{env}/add}")
    @ApiOperation(value = "向告警参数中新增配置项", notes = "向告警参数中新增配置项")
    public Object addParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO) {
        openItemDTO.setDataChangeCreatedBy(opUser);
        OpenItemDTO item = apolloClient.createItem(appId, env, cluster, namespace, openItemDTO);
        return JSON.toJSONString(item);
    }
 
    /**
     * 修改apollo中配置项，为未发布状态。
     * post uri:apollo/dev/update
     * @param env 指定apollo的数据环境
     * @return
     */
    @PutMapping("/{env}/update")
    @ApiOperation(value = "修改告警配置项", notes = "修改告警配置项")
    public Object updateParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO) {
        // http://{portal_address}/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/items/{key}
        OpenItemDTO openItemDTONew = new OpenItemDTO();
        openItemDTONew.setKey(openItemDTO.getKey());
        openItemDTONew.setValue(openItemDTO.getValue());
        openItemDTONew.setComment(openItemDTO.getComment());
        openItemDTONew.setDataChangeCreatedBy(opUser);
        openItemDTONew.setDataChangeLastModifiedTime(new Date());
        apolloClient.createOrUpdateItem(appId,env , cluster, namespace, openItemDTONew);
        return JSON.toJSONString(openItemDTONew);
    }
 
    /**
     * 删除apollo中配置项
     * post uri:apollo/dev/update
     * @param env 指定apollo的数据环境
     * @return
     */
    @DeleteMapping("/{env}/delete/{key}")
    @ApiOperation(value = "删除告警配置项", notes = "删除告警配置项")
    public void deleteParam(@PathVariable String env,@PathVariable String key) {
        // http://{portal_address}/openapi/v1/envs/{env}/apps/{appId}/clusters/{clusterName}/namespaces/{namespaceName}/items/{key}?operator={operator}
        apolloClient.removeItem(appId,env,cluster,namespace,key,opUser);
    }
 
    /**
     * 获取apollo中namespace的所有配置项
     * @param env 指定apollo的数据环境
     * @return
     */
    @GetMapping("/{env}/namespace")
    @ApiOperation(value = "获取告警参数所有配置项", notes = "获取告警参数所有配置项")
    public Object getAllNameSpace(@PathVariable String env) {
        return JSON.toJSONString(apolloClient.getNamespace(appId, env, cluster, namespace));
    }
 
 
    /**
     * 获取某一项配置
     * @param env 环境
     * @param key 配置项key
     * @return
     */
    @GetMapping("/{env}/getParam")
    @ApiOperation(value = "获取某一项配置", notes = "获取某一项配置")
    public Object getParam(String env,String key) {
        OpenItemDTO getItem = apolloClient.getItem(appId, env, cluster, namespace, key);
        return JSON.toJSONString(getItem);
    }
 
    /**
     * 发布配置
     * @param env
     * @return
     */
    @PostMapping("/{env}/release")
    @ApiOperation(value = "发布配置", notes = "发布配置")
    public Object releaseParam(String env) {
        NamespaceGrayDelReleaseDTO NamespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
        //配置版本名称
//        NamespaceGrayDelReleaseDTO.setReleaseTitle(DateUtil.getBJDateTime() + "-release");
        //刷新说明
        NamespaceGrayDelReleaseDTO.setReleaseComment("auto release");
        NamespaceGrayDelReleaseDTO.setReleasedBy(opUser);
        OpenReleaseDTO openReleaseDTO = apolloClient.publishNamespace(appId, env, cluster, namespace, NamespaceGrayDelReleaseDTO);
        return JSON.toJSONString(openReleaseDTO);
    }
}