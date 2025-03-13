package com.ncf.apollodemo.entity;

import lombok.Data;

/**
 * User类
 * 配置只能由开发人员添加（非leader开发人员修改配置需要发起OA审批不可直接修改后立即上线），
 * 产品只能修改有权限修改的配置（这些配置可以选择修改后立即发布，也可以发起OA审批，得到肯定回执后再发布）
 * leader添加或修改后可直接上线
 */
@Data
public class User {
    private Long id;

    private Long userId;

    private String userName;

    private String passWord;

    private String email;

    /**
     * 当用户权限为apollo负责人时，会有负责的appId，opsUser，token
     */
    private String roles;

    private Integer appId;

    private String apolloOpsUser;

    private String token;
}

