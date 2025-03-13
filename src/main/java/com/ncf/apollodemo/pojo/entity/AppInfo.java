package com.ncf.apollodemo.pojo.entity;

import lombok.Data;

@Data
public class AppInfo {

    private Integer appId;
    private String appName;
    //apollo应用负责人id
    private String userOpsId;

}
