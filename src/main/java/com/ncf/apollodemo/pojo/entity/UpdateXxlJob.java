package com.ncf.apollodemo.pojo.entity;

import lombok.Data;
import lombok.experimental.Accessors;
 
/**
 * 修改Job
 */
@Data
@Accessors(chain = true)
public class UpdateXxlJob {
    /**
     * 任务id
     */
    private int id;
    /**
     * 执行器主键ID
     */
    private int jobGroup;
    /**
     * job描述
     */
    private String jobDesc;
    /**
     * 负责人
     */
    private String author;
    /**
     * 调度类型
     */
    private String scheduleType;
    /**
     * 调度配置，值含义取决于调度类型
     */
    private String scheduleConf;
    /**
     * 执行器，任务Handler名称
     */
    private String executorHandler;
    /**
     * 调度过期策略
     */
    private String misfireStrategy;
    /**
     * 执行器路由策略
     */
    private String executorRouteStrategy;
    /**
     * 执行器，任务参数
     */
    private String executorParam;
    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;
 
}