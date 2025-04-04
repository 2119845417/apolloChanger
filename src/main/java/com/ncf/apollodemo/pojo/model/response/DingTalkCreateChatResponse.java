package com.ncf.apollodemo.pojo.model.response;

import lombok.Data;

/**
 * 创建群聊 API返对象
 */
@Data
public class DingTalkCreateChatResponse {
    private Integer errcode; // 错误码，0 表示成功
    private String errmsg; // 错误消息
    private String chatid; // 群 ID
    private String openConversationId; // 开放会话 ID
    private Integer conversationTag; // 会话标签
}