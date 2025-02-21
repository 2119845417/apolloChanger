package com.ncf.apollodemo.controller;

import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.resp.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 操作apollo配置
 * @author: lizz
 * @date: 2020/7/15 11:30
 */
@RestController
@RequestMapping(value = "apollo")
//@Profile({"dev","test"}) //内部环境使用
public class ApolloController {
    private static final Logger logger = LoggerFactory.getLogger(ApolloController.class);

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
    private ApolloOpenApiClient apolloClient;
    public ApolloController(ApolloOpenApiClient client) {
        this.apolloClient = client;
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }
    /**
     * 获取环境列表，如
     * [{"clusters":["huawei","default"],"env":"PRO"},{"clusters":["default"],"env":"DEV"}]
     * @param server apollo中服务id
     * @return
     */
    @GetMapping("/envclusters/{server}")
    public ResponseResult<List<OpenEnvClusterDTO>> getEnvclusters(@PathVariable String server) {
        logger.info("getEnvclusters server={}", server);
        try{
            List<OpenEnvClusterDTO> envClusterInfo = apolloClient.getEnvClusterInfo(server);
            return ResponseResult.success(envClusterInfo);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 向apollo中新增配置项，为未发布状态。
     * post uri:apollo/dev/add
     * @param env 指定apollo的数据环境
     * @return
     */
    @PostMapping("/{env}/add")
    public ResponseResult<OpenItemDTO> addParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO) {
        logger.info("addParam openItemDTO:{}", openItemDTO);
        openItemDTO.setDataChangeCreatedBy(opUser);
        try{
            OpenItemDTO item = apolloClient.createItem(appId, env, cluster, namespace, openItemDTO);
            return ResponseResult.success(item);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 修改apollo中配置项，为未发布状态。
     * post uri:apollo/dev/update
     * @param env 指定apollo的数据环境
     * @return
     */
    @PostMapping("/{env}/update")
    public ResponseResult<Boolean> updateParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO) {
        logger.info("updateParam openItemDTO:{}", openItemDTO);
        openItemDTO.setDataChangeCreatedBy(opUser);
        try{
            apolloClient.createOrUpdateItem(appId, env, cluster, namespace, openItemDTO);
            return ResponseResult.success(true);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 删除apollo中配置项，为未发布状态。
     * post uri:/LOCAL/removeByKey/{key}
     * @param env 指定apollo的数据环境
     * @return
     */
    @PostMapping("/{env}/removeByKey/{key}")
    public ResponseResult<Boolean> removeItem(@PathVariable String env, @PathVariable String key) {
        logger.info("removeItem key:{}", key);
        try{
            apolloClient.removeItem(appId, env, cluster, namespace, key, opUser);
            return ResponseResult.success(true);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 获取apollo中namespace的所有配置项
     * @param env 指定apollo的数据环境
     * @return
     */
    @GetMapping("/{env}/namespace")
    public ResponseResult<OpenNamespaceDTO> getAllNameSpace(@PathVariable String env) {
        logger.info("getAllNameSpace env:{}", env);
        try{
            OpenNamespaceDTO dto = apolloClient.getNamespace(appId, env, cluster, "application");
            return ResponseResult.success(dto);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }


    /**
     * 获取某一项配置
     * @param env 指定apollo的数据环境
     * @param key 配置项key
     * @return
     */
    @GetMapping("/{env}/getParam")
    public ResponseResult<OpenItemDTO> getParam(String env,String key) {
        logger.info("getParam key:{}", key);
        try{
            OpenItemDTO dto = apolloClient.getItem(appId, env, cluster, namespace, key);
            return ResponseResult.success(dto);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 刷新发布配置
     *
     * @param env 指定apollo的数据环境
     * @return
     */
     public ResponseResult<OpenReleaseDTO> releaseParam(String env) {
         logger.info("releaseParam env:{}", env);
         try {
             NamespaceGrayDelReleaseDTO namespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
             //配置版本名称
             namespaceGrayDelReleaseDTO.setReleaseTitle(System.currentTimeMillis() + "-release");
             //刷新说明
             namespaceGrayDelReleaseDTO.setReleaseComment("auto release");
             namespaceGrayDelReleaseDTO.setReleasedBy(opUser);
             OpenReleaseDTO openReleaseDTO = apolloClient.publishNamespace(appId, env, cluster, namespace, namespaceGrayDelReleaseDTO);
             return ResponseResult.success(openReleaseDTO);
         }catch (Exception e) {
             logger.error(e.getMessage());
             return ResponseResult.error(500, e.getMessage());
         }
    }
}