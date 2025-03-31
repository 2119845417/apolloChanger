package com.ncf.apollodemo.handler;

import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ncf.apollodemo.service.ApolloService;
import com.ncf.apollodemo.utils.XxlJobTemplate;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class XxlJobJobHandle {

    @Autowired
    private XxlJobTemplate xxlJobTemplate;

    @Autowired
    private ApolloService apolloService;

    @XxlJob("test-apollochanger")
    public ReturnT<String> xxlJobTest1(String date) {
        log.info("-------test-apollochanger--xxlJobTest定时任务执行成功--------");
        return ReturnT.SUCCESS;
    }

//    定时发布配置
    @XxlJob("apollochanger")
    public ReturnT<String> scheduledReleaseConf(String env, String appId) {
        log.info("-------apollochanger--定时发布配置--------");
        try {
            ApolloOpenApiClient client = apolloService.getClient(appId);
            apolloService.publishNamespace(env, appId, client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ReturnT.SUCCESS;
    }

}
