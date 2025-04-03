package com.ncf.apollodemo.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User类
 * 配置只能由开发人员添加（非leader开发人员修改配置需要发起OA审批不可直接修改后立即上线），
 * 产品只能修改有权限修改的配置（这些配置可以选择修改后立即发布，也可以发起OA审批，得到肯定回执后再发布）
 * leader添加或修改后可直接上线
 */
@Data
@TableName(value = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String passWord;

    @TableField(value = "name")
    private String name;

    @TableField(value = "department")
    private int department;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "is_active")
    private int isActive;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;
    /**
     * 当用户权限为apollo负责人时，会有负责的appId，opsUser，token
     */
//    private String roles;
//
//    private Integer appId;
//
//    private String apolloOpsUser;
//
//    private String token;
}

