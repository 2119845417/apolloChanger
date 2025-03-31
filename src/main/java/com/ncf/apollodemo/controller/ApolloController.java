package com.ncf.apollodemo.controller;

import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.config.ApolloClientRegistrar;
import com.ncf.apollodemo.pojo.entity.AddXxlJob;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @description: 操作apollo配置
 * @author: niucanfei
 * @date: 2025/2/18 11:30
 */
@RestController
@RequestMapping("/apollo")
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
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
            List<OpenItemDTO> openReleaseDTO = apolloService.getItemsByNamespace(appId,env, pageQueryDO.getPage(), pageQueryDO.getSize(),client);
            return ResponseResult.success(openReleaseDTO);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 非开发者得到某个appID某个环境下有权限的配置
     *
     * @param env 指定apollo的数据环境
     * @return
     */
    @GetMapping("/{env}/{appId}/getAuthAllKAndV")
    public ResponseResult<List<OpenItemDTO>> getAuthAllKeyAndV(@PathVariable String env, @PathVariable String appId, @RequestBody PageQueryDO pageQueryDO) {
        logger.info("getAllKeyAndV env:{}", env);
        try {
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
            List<OpenItemDTO> openReleaseDTO = apolloService.getItemsByNamespace(appId,env, pageQueryDO.getPage(), pageQueryDO.getSize(),client);
            if(!CollectionUtils.isEmpty(openReleaseDTO) && !openReleaseDTO.isEmpty()){
                //得到以FLAG-开头的配置项 这些配置项是有权限修改的
                List<OpenItemDTO> filteredItems = Optional.ofNullable(openReleaseDTO)
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .filter(dto -> dto != null && dto.getComment() != null)
                        .filter(dto -> dto.getComment().startsWith("FLAG-"))
                        .collect(Collectors.toList());
                return ResponseResult.success(filteredItems);
            }
            //返回空列表
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
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
//            List<OpenEnvClusterDTO> envClusterInfo = apolloClient.getEnvClusterInfo(server);
            List<OpenEnvClusterDTO> envClusterInfo = apolloService.getEnvclusters(server,appId,client);
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
            // 获取拦截器初始化的客户端
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
            OpenItemDTO item = apolloService.createItem(env, openItemDTO,appId,client);
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
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
            apolloService.createOrUpdateItem(env, openItemDTO,appId,client);
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
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
//            apolloClient.removeItem(appId, env, cluster, namespace, key, opUser);
            apolloService.removeItem(env, key,appId,client);
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
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
            OpenNamespaceDTO dto = apolloService.getNamespace(env,appId,client);
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
            ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
//            OpenItemDTO dto = apolloClient.getItem(appId, env, cluster, namespace, key);
            OpenItemDTO dto = apolloService.getItem(env, key,appId,client);
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
             ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                     .currentRequestAttributes()
                     .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
             OpenReleaseDTO openReleaseDTO = apolloService.publishNamespace(env,appId,client);
             return ResponseResult.success(openReleaseDTO);
         }catch (Exception e) {
             logger.error(e.getMessage());
             return ResponseResult.error(500, e.getMessage());
         }
    }

    @PostMapping("/{env}/{appId}/setTask")
    public ResponseResult<Integer> setTask(@PathVariable String env,@PathVariable String appId,@RequestBody AddXxlJob addXxlJob) {
        logger.info("setTask addXxlJob:{}", addXxlJob);
        Integer i = apolloService.setTask(addXxlJob);
        return null;
    }
}