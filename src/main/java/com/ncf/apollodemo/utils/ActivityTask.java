package com.ncf.apollodemo.utils;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ActivityTask {
    @Resource
    private ActivityMapper activityMapper;
 
    @XxlJob("updateActivityStatus")
    public void updateActivityStatus(){
        // 获取参数
        String param = XxlJobHelper.getJobParam();
        String[] methodParams = param.split(",");
        LocalDateTime now = LocalDateTime.now();
        UpdateWrapper<Activity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(Activity::getStatus, Integer.parseInt(methodParams[1]))
                .set(Activity::getUpdateTime, now)
                .eq(Activity::getId, Long.parseLong(methodParams[0])); // 添加唯一标识符
        activityMapper.update(null, updateWrapper);
    }
}