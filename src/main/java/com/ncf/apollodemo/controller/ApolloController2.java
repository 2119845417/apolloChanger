package com.ncf.apollodemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.NamespaceGrayDelReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenReleaseDTO;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 操作apollo配置
 * @author: lizz
 * @date: 2020/7/15 11:30
 */
@RestController
@RequestMapping(value = "apollo2")
//@Profile({"dev","test"}) //内部环境使用
public class ApolloController2 {
    private static final Logger logger = LoggerFactory.getLogger(ApolloController2.class);

    //apollo中项目id
    private final static String appId = "101";
    //apollo操作用户
    private final static String opUser = "apollo";
    //apollo中集群名称，apollo默认集群为default
    private final static String cluster = "default";
    //apollo中集群内namespace名称
    private final static String namespace = "application";

    //apollo操作客户端

    private ApolloOpenApiClient apolloClient;
    public ApolloController2(ApolloOpenApiClient client) {
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
    public Object getEnvclusters(@PathVariable String server) {
        return JSON.toJSONString(apolloClient.getEnvClusterInfo(server));
    }

    /**
     * 向apollo中新增配置项，为未发布状态。
     * post uri:apollo/dev/add
     * @param env 指定apollo的数据环境
     * @return
     */
//    @PostMapping("/{env}/add")
//    public Object addParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO) {
//        openItemDTO.setDataChangeCreatedBy(opUser);
//        OpenItemDTO item = apolloClient.createItem(appId, env, cluster, namespace, openItemDTO);
//        return JSON.toJSONString(item);
//    }
    @PostMapping("/{env}/add")
    public Object addParam(@PathVariable String env) {
        OpenItemDTO openItemDTO = new OpenItemDTO();
        openItemDTO.setKey("testceshi");
        openItemDTO.setValue("999999");
        openItemDTO.setComment("测试");
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
    @PostMapping("/{env}/update")
    public Object updateParam(@PathVariable String env,@RequestBody OpenItemDTO openItemDTO) {
        openItemDTO.setDataChangeCreatedBy(opUser);
        apolloClient.createOrUpdateItem(appId, env, cluster, namespace, openItemDTO);
        return JSON.toJSONString(openItemDTO);
    }

    /**
     * 获取apollo中namespace的所有配置项
     * @param env 指定apollo的数据环境
     * @return
     */
    @GetMapping("/{env}/namespace")
    public Object getAllNameSpace(@PathVariable String env) {
        return JSON.toJSONString(apolloClient.getNamespace(appId, env, cluster, "application"));
    }


    /**
     * 获取某一项配置
     * @param env 指定apollo的数据环境
     * @param key 配置项key
     * @return
     */
    @GetMapping("/{env}/getParam")
    public Object getParam(String env,String key) {
        OpenItemDTO getItem = apolloClient.getItem(appId, env, cluster, namespace, key);
        return JSON.toJSONString(getItem);
    }

    /**
     * 刷新发布配置
     *
     * @param env 指定apollo的数据环境
     * @return
     */
     public Object releaseParam(String env) {
        NamespaceGrayDelReleaseDTO namespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
        //配置版本名称
        namespaceGrayDelReleaseDTO.setReleaseTitle(System.currentTimeMillis() + "-release");
        //刷新说明
        namespaceGrayDelReleaseDTO.setReleaseComment("auto release");
        namespaceGrayDelReleaseDTO.setReleasedBy(opUser);
        OpenReleaseDTO openReleaseDTO = apolloClient.publishNamespace(appId, env, cluster, namespace, namespaceGrayDelReleaseDTO);
        return JSON.toJSONString(openReleaseDTO);
    }
}