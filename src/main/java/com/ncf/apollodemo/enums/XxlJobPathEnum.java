package com.ncf.apollodemo.enums;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum XxlJobPathEnum {
    LOGIN("/login", "登录"),
    ADD("/jobinfo/add", "添加Job"),
    STOP("/jobinfo/stop","停止Job"),
    UPDATE("/jobinfo/update", "更新Job"),
    REMOVE("/jobinfo/remove", "删除Job"),
    PAGE_JOB("/jobinfo/pageList", "查询Job"),
    PAGE_GROUP("/jobgroup/pageList", "查询Job组");
 
    private String path;
    private String desc;
}