package com.ncf.apollodemo.pojo.model.request;

import lombok.Data;

import java.util.List;

/**
 * 创建群聊API请求对象
 */
@Data
public class DingTalkCreateChatRequest {
    private String name; // 群名称
    private String owner; // 群主的用户 ID (说明,该员工必须为会话useridlist的成员之一。)
    private List<String> useridlist; // 群成员的用户 ID 列表(群成员列表，每次最多支持40人，群人数上限为1000。可通过根据手机号查询用户接口获取userid参数值。)
    private Integer showHistoryType; // 是否显示历史消息(新成员是否可查看100条历史消息：1：可查看,0：不可查看,如果不传值，代表不可查看。)
    private Integer searchable; // 是否可搜索群是否可以被搜索：0（默认）：不可搜索,1：可搜索
    private Integer validationType; // 验证类型入群是否需要验证：0（默认）：不验证,1：入群验证
    private Integer mentionAllAuthority; // @全体成员的权限@all 使用范围：0（默认）：所有人可使用,1：仅群主可@all
    private Integer managementType; // 群管理类型群管理类型：0（默认）：所有人可管理1：仅群主可管理
    private Integer chatBannedType; // 群禁言类型是否开启群禁言：0（默认）：不禁言,1：全员禁言
}