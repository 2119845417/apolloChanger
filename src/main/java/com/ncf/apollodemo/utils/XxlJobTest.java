package com.ncf.apollodemo.utils;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class XxlJobTest {
    
    @XxlJob("test-apollochanger")
    public ReturnT<String> xxlJobTest1(String date) {
        log.info("-------test-apollochanger--xxlJobTest定时任务执行成功--------");
        return ReturnT.SUCCESS;
    }

    @XxlJob("apollochanger")
    public ReturnT<String> xxlJobTest2(String date) {
        log.info("-------apollochanger--xxlJobTest定时任务执行成功--------");
        return ReturnT.SUCCESS;
    }

}
