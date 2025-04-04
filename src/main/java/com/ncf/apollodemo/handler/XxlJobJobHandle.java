package com.ncf.apollodemo.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.google.common.base.Splitter;
import com.ncf.apollodemo.pojo.domain.XxlJobInfo;
import com.ncf.apollodemo.manager.service.ApolloService;
import com.ncf.apollodemo.utils.XxlJobTemplate;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class XxlJobJobHandle {

    @Autowired
    private XxlJobTemplate xxlJobTemplate;

    @Autowired
    private ApolloService apolloService;


//    用于平台用户定时发布配置
    @XxlJob("scheduledReleaseConf")
    @ConditionalOnProperty(name = "xxl.job.enabled", havingValue = "true")
    public ReturnT<String> scheduledReleaseConf() {
        log.info("-------apollochanger--定时发布任务--------");
        try {
            // 获取任务参数
            String param = XxlJobHelper.getJobParam();
            if (StringUtils.isBlank(param)) {
                throw new RuntimeException("任务参数不能为空");
            }

            // 解析键值对参数（如 env=LOCAL&appId=101）
            Map<String, String> paramMap = Splitter.on('&')
                    .withKeyValueSeparator('=')
                    .split(param);

            String env = paramMap.get("env");
            String appId = paramMap.get("appId");
            ApolloOpenApiClient client = apolloService.getClient(appId);
            apolloService.publishNamespace(env, appId, client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ReturnT.SUCCESS;
    }

    @XxlJob("deleteExpiredTask")
    @ConditionalOnProperty(name = "xxl.job.enabled", havingValue = "true")
    public ReturnT<String> deleteExpiredTask() {
        log.info("-------apollochanger--每天0点删除已过期的任务--------");
        try {
            List<XxlJobInfo> removeList = xxlJobTemplate.listJob(1, 0, "创建定时发布任务", "scheduledReleaseConf", "ApolloChangerTEAM");
            log.info("删除的任务：" + removeList.toString());
            if(!CollectionUtils.isEmpty(removeList)){
                for (XxlJobInfo xxlJobInfo : removeList) {
                    xxlJobTemplate.removeJob(xxlJobInfo.getId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ReturnT.SUCCESS;
    }
}
