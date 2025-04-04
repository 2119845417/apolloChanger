package com.ncf.apollodemo.pojo.model.response;

import lombok.Data;

/**
 * 用户信息请求API返回对象
 */
@Data
public class DingTalkUserResponse {
    private Integer errcode; // 错误码，0 表示成功
    private String errmsg; // 错误消息
    private Result result; // 结果对象
    private String requestId; // 请求 ID

    @Data
    public static class Result {
        private String userid; // 用户 ID
    }
}