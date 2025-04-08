package com.ncf.apollodemo.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName(value = "apollo_client_config")
@AllArgsConstructor
@NoArgsConstructor
public class ApolloClientConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "app_id")
    private String appid;
    @TableField(value = "token")
    private String token;
    @TableField(value = "portal_url")
    private String portalUrl;
    @TableField(value = "owner")
    private String owner;
    @TableField(value = "opUser")
    private String opUser;
    @TableField(value = "cluster")
    private String cluster;
    @TableField(value = "namespace")
    private String namespace;
    @TableField(value = "is_active")
    private int isActive;
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    @TableField(value = "modify_time")
    private LocalDateTime modifyTime;
}
