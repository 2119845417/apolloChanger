package com.ncf.apollodemo.controller;

import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.config.ApolloClientRegistrar;
import com.ncf.apollodemo.manager.service.DingTalkService;
import com.ncf.apollodemo.pojo.domain.AddXxlJob;

import com.ncf.apollodemo.pojo.dto.CallBackDTO;
import com.ncf.apollodemo.pojo.dto.CreateOrUpdateDTO;
import com.ncf.apollodemo.pojo.dto.PageQueryDTO;
import com.ncf.apollodemo.pojo.model.request.initcard.PrivateCardInitRequest;
import com.ncf.apollodemo.pojo.model.response.AccessTokenResponse;
import com.ncf.apollodemo.pojo.model.response.CardInstanceResponse;
import com.ncf.apollodemo.pojo.model.response.DingTalkUserResponse;
import com.ncf.apollodemo.resp.ResponseResult;
import com.ncf.apollodemo.manager.service.ApolloService;
import com.ncf.apollodemo.manager.service.TokenService;
import com.ncf.apollodemo.utils.SingletonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private DingTalkService dingTalkService;


    /**
     * 得到某个appID某个环境下所有配置
     *
     * @param env 指定apollo的数据环境
     * @return
     */
    @GetMapping("/{env}/{appId}/getAllKAndV")
    public ResponseResult<List<OpenItemDTO>> getAllKeyAndV(@PathVariable String env, @PathVariable String appId, @RequestBody PageQueryDTO pageQueryDO) {
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
    public ResponseResult<List<OpenItemDTO>> getAuthAllKeyAndV(@PathVariable String env, @PathVariable String appId, @RequestBody PageQueryDTO pageQueryDO) {
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
    public ResponseResult<OpenItemDTO> addParam(@PathVariable String env,@PathVariable String appId,@RequestBody CreateOrUpdateDTO createOrUpdateDTO) {
        logger.info("addParam scheduledUpdateDTO:{}", createOrUpdateDTO);
        try{
            if(env.equals("PR") || env.equals("PROD")){
            //            发送卡片，监听卡片结果
            AccessTokenResponse accessTokenData = dingTalkService.getAccessToken();
            String accessToken = accessTokenData.getAccessToken();
//            正确做法是根据appid找到对应负责人，然后去user.phone传入
            DingTalkUserResponse idData = dingTalkService.getUserByMobile(accessToken, createOrUpdateDTO.getPhone());
            String userid = idData.getResult().getUserid();
            PrivateCardInitRequest request = new PrivateCardInitRequest(userid,appId,createOrUpdateDTO);
            CardInstanceResponse.Result result = dingTalkService.initPrivateCard(accessToken, request);
//            向单例共享map里保存 卡片id为key，配置变更信息为value的键值对
                CallBackDTO callBackDTO = new CallBackDTO(appId,env,createOrUpdateDTO);
                SingletonMap.getInstance().put(result.getOutTrackId(), callBackDTO);
            return ResponseResult.success(createOrUpdateDTO.getOpenItemDTO());
        }else {
                // 获取拦截器初始化的客户端
                ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                        .currentRequestAttributes()
                        .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
                OpenItemDTO item = apolloService.createItem(env, createOrUpdateDTO.getOpenItemDTO(),appId,client);
                //                设置一次性定时任务
                if(createOrUpdateDTO.getAddXxlJob() == null){
                    apolloService.publishNamespace(env,appId,client);
                }else {
                    apolloService.setTask(createOrUpdateDTO.getAddXxlJob(), env, appId);
                }
                return ResponseResult.success(item);
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    /**
     * 修改apollo中配置项，为未发布状态。需指定发布时间
     * post uri:apollo/dev/update
     * @param env 指定apollo的数据环境
     * @return
     */
    @PostMapping("/{env}/{appId}/update")
    public ResponseResult<Boolean> update(@PathVariable String env, @PathVariable String appId, @RequestBody CreateOrUpdateDTO createOrUpdateDTO) {
        logger.info("updateParam createOrUpdateDTO:{}", createOrUpdateDTO);
        try{
            if(env.equals("PR") || env.equals("PROD")){
                //            发送卡片，监听卡片结果
                AccessTokenResponse accessTokenData = dingTalkService.getAccessToken();
                String accessToken = accessTokenData.getAccessToken();
//            正确做法是根据appid找到对应负责人，然后去user.phone传入
                DingTalkUserResponse idData = dingTalkService.getUserByMobile(accessToken, createOrUpdateDTO.getPhone());
                String userid = idData.getResult().getUserid();
                PrivateCardInitRequest request = new PrivateCardInitRequest(userid,appId,createOrUpdateDTO);
                CardInstanceResponse.Result result = dingTalkService.initPrivateCard(accessToken, request);
//            向单例共享map里保存 卡片id为key，配置变更信息为value的键值对
                CallBackDTO callBackDTO = new CallBackDTO(appId,env,createOrUpdateDTO);
                SingletonMap.getInstance().put(result.getOutTrackId(), callBackDTO);
                return ResponseResult.success(true);
            }else {
                ApolloOpenApiClient client = (ApolloOpenApiClient) RequestContextHolder
                        .currentRequestAttributes()
                        .getAttribute("apolloClient", RequestAttributes.SCOPE_REQUEST);
                apolloService.createOrUpdateItem(env, createOrUpdateDTO.getOpenItemDTO(),appId,client);
//                设置一次性定时任务
                if(createOrUpdateDTO.getAddXxlJob() == null){
                    apolloService.publishNamespace(env,appId,client);
                }else {
//                    定时发布
                    apolloService.setTask(createOrUpdateDTO.getAddXxlJob(), env, appId);
                }
            }
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
        try{
            Integer i = apolloService.setTask(addXxlJob,env,appId);
            return ResponseResult.success(i);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }

    @PostMapping("/{env}/{appId}/sendCard")
    public ResponseResult<String> sendCard(@PathVariable String env, @PathVariable String appId, @RequestBody CreateOrUpdateDTO createOrUpdateDTO) {
        logger.info("sendCard createOrUpdateDTO:{}", createOrUpdateDTO);
        try{
//            发送卡片，监听卡片结果
            AccessTokenResponse accessTokenData = dingTalkService.getAccessToken();
            String accessToken = accessTokenData.getAccessToken();
//            正确做法是根据appid找到对应负责人，然后去user.phone传入
            DingTalkUserResponse idData = dingTalkService.getUserByMobile(accessToken, createOrUpdateDTO.getPhone());
            String userid = idData.getResult().getUserid();
            PrivateCardInitRequest request = new PrivateCardInitRequest(userid,appId,createOrUpdateDTO);
            CardInstanceResponse.Result result = dingTalkService.initPrivateCard(accessToken, request);
//            向单例共享map里保存 卡片id为key，配置变更信息为value的键值对
            CallBackDTO callBackDTO = new CallBackDTO(appId,env, createOrUpdateDTO);
            SingletonMap.getInstance().put(result.getOutTrackId(), callBackDTO);
            return ResponseResult.success(result.getOutTrackId());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseResult.error(500, e.getMessage());
        }
    }
}