package com.ncf.apollodemo.pojo.model.request.initcard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 单发卡片初始化请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PrivateCardInitRequest extends BaseDingTalkInitCardRequest {

    private PrivateSpaceModel imRobotOpenSpaceModel;// IM机器人单聊场域信息。
    private PrivateDeliverModel imRobotOpenDeliverModel;//// IM机器人单聊投放参数。

    public PrivateCardInitRequest(String userId) {
        this.openSpaceId = "dtv1.card//IM_ROBOT." + userId;
        this.imRobotOpenSpaceModel = new PrivateSpaceModel(true);
        this.imRobotOpenDeliverModel = new PrivateDeliverModel("IM_ROBOT");
    }

    @Data
    @AllArgsConstructor
    public static class PrivateSpaceModel {
        private Boolean supportForward;//卡片消息是否支持转发默认为true
    }

    @Data
    @AllArgsConstructor
    public static class PrivateDeliverModel {
        private String spaceType;//场域类型
    }
}