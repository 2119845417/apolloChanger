package com.ncf.apollodemo.utils;

import com.ncf.apollodemo.enums.ActivityConstant;
import com.ncf.apollodemo.pojo.entity.AddXxlJob;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
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
     * 活动状态更改定时任务(添加活动时调用)
     * @param activity
     * @return
     */
 
//    public void addActivityStatusTask(Activity activity,Integer groupId){//第二个参数为执行器主键ID
//        //获取到各个需要进行状态转换的时间
//        LocalDateTime beginTime = activity.getBeginTime();
//        LocalDateTime endTime = activity.getEndTime();
//        LocalDateTime postTime = activity.getPostTime();
//        LocalDateTime signBeginTime = activity.getSignBeginTime();
//        LocalDateTime signEndTime = activity.getSignEndTime();
//        Long activityId = activity.getId();
//
//        //生成相应cron表达式
//        String cronToStatusTo1 = generateCron(postTime);
//        String cronToStatusTo2 = generateCron(signBeginTime);
//        String cronToStatusTo3 = generateCron(signEndTime);
//        String cronToStatusTo4 = generateCron(beginTime);
//        String cronToStatusTo5 = generateCron(endTime);
//
//        //添加任务
//        addJobForActivity(groupId,"活动状态转为1",cronToStatusTo1,"updateActivityStatus",
//                activityId, ActivityConstant.ACTIVITY_PARTICIPATION_STATUS_PARTICIPATED);
//        addJobForActivity(groupId,"活动状态转为2",cronToStatusTo2,"updateActivityStatus",
//                activityId, ActivityConstant.ACTIVITY_STATUS_SIGN_UP);
//        addJobForActivity(groupId,"活动状态转为3",cronToStatusTo3,"updateActivityStatus",
//                activityId, ActivityConstant.ACTIVITY_WAITING_FOR_START);
//        addJobForActivity(groupId,"活动状态转为4",cronToStatusTo4,"updateActivityStatus",
//                activityId, ActivityConstant.ACTIVITY_IN_PROGRESS);
//        addJobForActivity(groupId,"活动状态转为5",cronToStatusTo5,"updateActivityStatus",
//                activityId, ActivityConstant.ACTIVITIES_TO_BE_CERTIFIED);
//
//        log.info("添加活动状态转换定时任务成功");
//    }
 
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