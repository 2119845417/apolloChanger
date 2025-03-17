package com.ncf.apollodemo.controller;

import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.config.ApolloClientRegistrar;
import com.ncf.apollodemo.pojo.queryDO.PageQueryDO;
import com.ncf.apollodemo.resp.ResponseResult;
import com.ncf.apollodemo.service.ApolloService;
import com.ncf.apollodemo.service.impl.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 操作apollo配置
 * @author: niucanfei
 * @date: 2025/2/18 11:30
 */
@RestController
@RequestMapping(value = "apollo")
//@Profile({"dev","test"}) //内部环境使用
public class ApolloController {
    private static final Logger logger = LoggerFactory.getLogger(ApolloController.class);
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ApolloService apolloService;
    @Autowired
    private ApolloClientRegistrar beanRegistrar;
    @Autowired
    private TokenService tokenService;

    //apollo操作客户端
    private ApolloOpenApiClient apolloClient;


    /**
     * 得到某个appID某个环境下所有配置
     *
     * @param env 指定apollo的数据环境
     * @return
     */
    @GetMapping("/{env}/{appId}/getAllKAndV")
    public ResponseResult<List<OpenItemDTO>> getAllKeyAndV(@PathVariable String env, @PathVariable String appId, @RequestBody PageQueryDO pageQueryDO) {
        logger.info("getAllKeyAndV env:{}", env);
        try {
            List<OpenItemDTO> openReleaseDTO = apolloService.getItemsByNamespace(appId,env, pageQueryDO.getPage(), pageQueryDO.getSize());
            return ResponseResult.success(openReleaseDTO);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }


    @PostMapping("/getClient")
    public ResponseResult<ApolloOpenApiClient> getClient(@RequestParam String appId) {
        // 检查Bean是否已存在
        String beanName = "apolloClient_" + appId;
        if (!context.containsBean(beanName)) {
            String token = tokenService.getTokenByAppId(appId);
            beanRegistrar.registerClientBean(appId, token); // 动态注册
        }
        ApolloOpenApiClient client = context.getBean(beanName, ApolloOpenApiClient.class);
        return ResponseResult.success(client);
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
    @GetMapping("{appId}/envclusters/{server}")
    public ResponseResult<List<OpenEnvClusterDTO>> getEnvclusters(@PathVariable String server,@PathVariable String appId) {
        logger.info("getEnvclusters server={}", server);
        try{
//            List<OpenEnvClusterDTO> envClusterInfo = apolloClient.getEnvClusterInfo(server);
            List<OpenEnvClusterDTO> envClusterInfo = apolloService.getEnvclusters(server,appId);
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
    @PostMapping("/{env}/{appId}/add")
    public ResponseResult<OpenItemDTO> addParam(@PathVariable String env,@PathVariable String appId,@RequestBody OpenItemDTO openItemDTO) {
        logger.info("addParam openItemDTO:{}", openItemDTO);
        try{
            OpenItemDTO item = apolloService.createItem(env, openItemDTO,appId);
//            openItemDTO.setDataChangeCreatedBy(opUser);
//            OpenItemDTO item = apolloClient.createItem(appId, env, cluster, namespace, openItemDTO);
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
    @PostMapping("/{env}/{appId}/update")
    public ResponseResult<Boolean> updateParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO,@PathVariable String appId) {
        logger.info("updateParam openItemDTO:{}", openItemDTO);
        try{
            apolloService.createOrUpdateItem(env, openItemDTO,appId);
//            openItemDTO.setDataChangeCreatedBy(opUser);
//            apolloClient.createOrUpdateItem(appId, env, cluster, namespace, openItemDTO);
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
    @PostMapping("/{env}/{appId}/removeByKey/{key}")
    public ResponseResult<Boolean> removeItem(@PathVariable String env, @PathVariable String key,@PathVariable String appId) {
        logger.info("removeItem key:{}", key);
        try{
//            apolloClient.removeItem(appId, env, cluster, namespace, key, opUser);
            apolloService.removeItem(env, key,appId);
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
    @GetMapping("/{env}/{appId}/namespace")
    public ResponseResult<OpenNamespaceDTO> getAllNameSpace(@PathVariable String env,@PathVariable String appId) {
        logger.info("getAllNameSpace env:{}", env);
        try{
            OpenNamespaceDTO dto = apolloService.getNamespace(env,appId);
//            OpenNamespaceDTO dto = apolloClient.getNamespace(appId, env, cluster, "application");
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
    @GetMapping("/{env}/{appId}/getParam/{key}")
    public ResponseResult<OpenItemDTO> getParam(@PathVariable String env,@PathVariable String key,@PathVariable String appId) {
        logger.info("getParam key:{}", key);
        try{
//            OpenItemDTO dto = apolloClient.getItem(appId, env, cluster, namespace, key);
            OpenItemDTO dto = apolloService.getItem(env, key,appId);
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
    @GetMapping("/{env}/{appId}/release")
     public ResponseResult<OpenReleaseDTO> releaseParam(@PathVariable String env,@PathVariable String appId) {
         logger.info("releaseParam env:{}", env);
         try {
//             NamespaceGrayDelReleaseDTO namespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
//             //配置版本名称
//             namespaceGrayDelReleaseDTO.setReleaseTitle(System.currentTimeMillis() + "-release");
//             //刷新说明
//             namespaceGrayDelReleaseDTO.setReleaseComment("auto release");
//             namespaceGrayDelReleaseDTO.setReleasedBy(opUser);
//             OpenReleaseDTO openReleaseDTO = apolloClient.publishNamespace(appId, env, cluster, namespace, namespaceGrayDelReleaseDTO);
             OpenReleaseDTO openReleaseDTO = apolloService.publishNamespace(env,appId);
             return ResponseResult.success(openReleaseDTO);
         }catch (Exception e) {
             logger.error(e.getMessage());
             return ResponseResult.error(500, e.getMessage());
         }
    }
}