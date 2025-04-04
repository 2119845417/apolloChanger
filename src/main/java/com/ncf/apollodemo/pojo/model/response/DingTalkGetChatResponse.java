package com.ncf.apollodemo.pojo.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 获取群聊信息API返回对象
 */
@Data
public class DingTalkGetChatResponse {
    private Integer errcode; // 错误码，0 表示成功
    private String errmsg; // 错误消息

    @JsonProperty("chat_info")
    private ChatInfo chatInfo; // 群信息

    @Data
    public static class ChatInfo {
        private String owner; // 群主的用户 ID
        private Integer showHistoryType; // 是否显示历史消息
        private String chatid; // 群 ID
        private Integer validationType; // 验证类型
        private List<String> useridlist; // 群成员的用户 ID 列表
        private String icon; // 群头像
        private String openConversationId; // 开放会话 ID
        private Integer searchable; // 是否可搜索
        private Integer chatBannedType; // 群禁言类型
        private Integer managementType; // 群管理类型
        private Integer mentionAllAuthority; // @全体成员的权限
        private Integer conversationTag; // 会话标签
        private String name; // 群名称
        private Integer status; // 群状态
    }
}