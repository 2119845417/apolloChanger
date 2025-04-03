package com.ncf.apollodemo.manager.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.dto.*;
import com.ncf.apollodemo.config.ApolloClientRegistrar;
import com.ncf.apollodemo.manager.service.ApolloService;
import com.ncf.apollodemo.pojo.domain.AddXxlJob;
import com.ncf.apollodemo.utils.CronGenerator;
import com.ncf.apollodemo.utils.XxlJobTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.scheduling.support.CronExpression;

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

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApolloClientRegistrar beanRegistrar;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private XxlJobTemplate xxlJobTemplate;

    @Override
    public List<OpenEnvClusterDTO> getEnvclusters(String server,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;
        List<OpenEnvClusterDTO> envClusterInfo = apolloClient.getEnvClusterInfo(server);
        return envClusterInfo;
    }

    @Override
    public OpenItemDTO createItem(String env,OpenItemDTO itemDTO,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;
        itemDTO.setDataChangeCreatedBy(opUser);
        OpenItemDTO item = apolloClient.createItem(appId, env, cluster, namespace, itemDTO);
        return item;
    }

    @Override
    public void createOrUpdateItem(String env, OpenItemDTO itemDTO,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;

        itemDTO.setDataChangeCreatedBy(opUser);
        apolloClient.createOrUpdateItem(appId, env, cluster, namespace, itemDTO);
    }

    @Override
    public void removeItem(String env, String key,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;

        apolloClient.removeItem(appId, env, cluster, namespace, key, opUser);
    }

    @Override
    public OpenNamespaceDTO getNamespace(String env,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;

        OpenNamespaceDTO openNamespaceDTO = apolloClient.getNamespace(appId, env, cluster, namespace);
        return openNamespaceDTO;
    }

    @Override
    public OpenItemDTO getItem(String env, String key,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;

        OpenItemDTO dto = apolloClient.getItem(appId, env, cluster, namespace, key);
        return dto;
    }

    @Override
    public OpenReleaseDTO publishNamespace(String env,String appId,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;

        NamespaceGrayDelReleaseDTO namespaceGrayDelReleaseDTO = new NamespaceGrayDelReleaseDTO();
        //配置版本名称
        namespaceGrayDelReleaseDTO.setReleaseTitle(System.currentTimeMillis() + "-release");
        //刷新说明
        namespaceGrayDelReleaseDTO.setReleaseComment("auto release");
        namespaceGrayDelReleaseDTO.setReleasedBy(opUser);
        OpenReleaseDTO openReleaseDTO = apolloClient.publishNamespace(appId, env, cluster, namespace, namespaceGrayDelReleaseDTO);
        return openReleaseDTO;
    }

    @Override
    public List<OpenItemDTO> getItemsByNamespace(String appId, String env,int page, int size,ApolloOpenApiClient client) {
        ApolloOpenApiClient apolloClient = client;

        OpenNamespaceDTO namespaceDTO = apolloClient.getNamespace(appId, env, cluster, namespace);
        return namespaceDTO.getItems();
    }

    @Override
    public Integer setTask(AddXxlJob addXxlJob,String env,String appId) {
        // 基础参数校验
        validateRequiredParams(addXxlJob);
        String scheduleConf = addXxlJob.getScheduleConf();
        String cronExpression;

        // 输入为具体时间 → 生成一次性 Cron
        if (isDateTimeFormat(scheduleConf)) {
            // 时间参数处理与校验
            LocalDateTime dateTime = parseAndValidateTime(scheduleConf);
            cronExpression = CronGenerator.generateCronExpression(dateTime); // 生成仅执行一次的 Cron[1,3](@ref)
        }
        // 输入为 Cron → 直接使用
        else if (isValidCronExpression(scheduleConf)) {
            cronExpression = scheduleConf;
        }
        // 非法输入
        else {
            throw new IllegalArgumentException("参数 scheduleConf 必须是有效时间或合法 Cron 表达式");
        }

        // 构建任务参数
        AddXxlJob xxlJob = new AddXxlJob()
                .setJobGroup(addXxlJob.getJobGroup())
                .setJobDesc(addXxlJob.getJobDesc())
                .setAuthor("ApolloChangerTEAM")
                .setScheduleType("CRON")
                .setScheduleConf(cronExpression)
                .setExecutorHandler(addXxlJob.getExecutorHandler())
                .setExecutorParam("env=" + env + "&" + "appId=" + appId);
        return xxlJobTemplate.addJob(xxlJob);
    }

    private void validateRequiredParams(AddXxlJob job) {
        if (job.getJobGroup() <= 0) {
            throw new IllegalArgumentException("任务分组ID非法");
        }
        if (StringUtils.isBlank(job.getExecutorHandler())) {
            throw new IllegalArgumentException("执行器Handler不能为空");
        }
        if (StringUtils.isBlank(job.getScheduleConf())) {
            throw new IllegalArgumentException("调度时间不能为空");
        }
    }

    private LocalDateTime parseAndValidateTime(String scheduleConf) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(scheduleConf, formatter);
            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("定时时间不能早于当前时间");
            }
            return dateTime;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("时间格式必须为yyyy-MM-dd HH:mm:ss");
        }
    }


    // 判断是否为有效时间格式（如 "yyyy-MM-dd HH:mm:ss"）
    private boolean isDateTimeFormat(String scheduleConf) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime.parse(scheduleConf, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // 判断是否为合法 Cron 表达式
    private boolean isValidCronExpression(String scheduleConf) {
        return CronExpression.isValidExpression(scheduleConf);
    }


    @Override
    public ApolloOpenApiClient getClient(String appId) {
        // 检查Bean是否已存在
        String beanName = "apolloClient_" + appId;
        if (!context.containsBean(beanName)) {
            String token = tokenService.getTokenByAppId(appId);
            beanRegistrar.registerClientBean(appId, token); // 动态注册
        }
        ApolloOpenApiClient client = context.getBean(beanName, ApolloOpenApiClient.class);
        return client;
    }


}
