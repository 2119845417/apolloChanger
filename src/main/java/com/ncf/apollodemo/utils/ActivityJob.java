package com.ncf.apollodemo.utils;

import com.ncf.apollodemo.pojo.domain.AddXxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
 
@Slf4j
@Component
public class ActivityJob {
 
    private XxlJobTemplate xxlJobTemplate;
 
    @Autowired
    public ActivityJob(XxlJobTemplate xxlJobTemplate) {
        this.xxlJobTemplate = xxlJobTemplate;
    }
 
    // 如果需要支持手动设置依赖
    public void setXxlJobTemplate(XxlJobTemplate xxlJobTemplate) {
        this.xxlJobTemplate = xxlJobTemplate;
    }

 
    /**
     * 添加定时任务
     */
    public void addJobForActivity(Integer groupId,String describe,String cron,String executorHandler,
                           Long activityId,Integer targetStatus) {
        AddXxlJob addXxlJob = new AddXxlJob()
                .setJobGroup(groupId)
                .setJobDesc(describe)
                .setAuthor("wmt")
                .setScheduleType("CRON")
                .setScheduleConf(cron)
                .setExecutorHandler(executorHandler)
                .setExecutorParam(activityId + "," +targetStatus);
        Integer jobId = xxlJobTemplate.addJob(addXxlJob);
        System.out.println("添加成功");
        System.out.println(jobId);
    }
 
    // 生成一次性 CRON 表达式
    public static String generateCron(LocalDateTime dateTime) {
        // CRON 格式：秒 分 时 日 月 ? 年
        return String.format("%d %d %d %d %d ? %d",
                dateTime.getSecond(),          // 秒
                dateTime.getMinute(),          // 分
                dateTime.getHour(),            // 时
                dateTime.getDayOfMonth(),      // 日
                dateTime.getMonthValue(),       // 月
                dateTime.getYear()              // 年
        );
    }
}