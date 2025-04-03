package com.ncf.apollodemo.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CronGenerator {

    public static String cronStr(LocalDateTime baseTime,long h,long m) {
        // 计算计划时间h小时m分钟后的时间点
        LocalDateTime delayTime = baseTime.plusHours(h).plusMinutes(m);
        // 创建Cron表达式
        String cronExpression = generateCronExpression(delayTime);
        log.info("Cron expression for [h] hours and [m] minutes from baseTime: " + cronExpression);
        return cronExpression;
    }

    public static String generateCronExpression(LocalDateTime dateTime) {
        // 将LocalDateTime转换为Cron表达式的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy");
        // 生成Cron表达式
        return dateTime.format(formatter);
    }
}

