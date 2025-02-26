package com.ncf.apollodemo.dto;

import lombok.Data;

@Data
public class AppInfo {

    private Integer appId;
    private String appName;
    //apollo应用负责人名称
    private String apolloLeaderName;

}
