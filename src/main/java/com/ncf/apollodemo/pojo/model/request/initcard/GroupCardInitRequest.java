package com.ncf.apollodemo.pojo.model.request.initcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 群发卡片初始化请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupCardInitRequest extends BaseDingTalkInitCardRequest {

    private GroupSpaceModel imGroupOpenSpaceModel;// IM群聊场域信息。
    private GroupDeliverModel imGroupOpenDeliverModel;//群聊投放参数。

    public GroupCardInitRequest(String openConversationId) {
        this.openSpaceId = "dtv1.card//IM_GROUP." + openConversationId;
        this.imGroupOpenSpaceModel = new GroupSpaceModel(true);
    }

    @Data
    @AllArgsConstructor
    public static class GroupSpaceModel {
        private Boolean supportForward;//是否支持转发
    }

    @Data
    @AllArgsConstructor
    public static class GroupDeliverModel {
        private String robotCode;//机器人编码
    }
}
