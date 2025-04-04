package com.ncf.apollodemo.pojo.model.request.initcard;

import lombok.Data;

@Data
public  class BaseDingTalkInitCardRequest {

    private String cardTemplateId;//卡片内容模板ID，可通过登录开发者后台 > 卡片平台获取。

    private String outTrackId;// 外部卡片实例Id。自己生成并作为入参传递给钉钉,一个 outTrackId 唯一标识一张卡片，

    private String callbackType; // 回调类型卡片回调的类型：,"STREAM"：stream模式,"HTTP"：http模式

    private CardData cardData;//卡片数据内容

    public String openSpaceId;//开放空间ID
    // 嵌套卡片数据结构
    @Data
    public static class CardData {
        private CardParamMap cardParamMap;
    }
    // 卡片参数映射（根据需要自行修改）
    @Data
    public static class CardParamMap {
        private String createTime;
        private String title;
        private String type;
        private String reason;
        private String lastMessage;
        private String status;
    }
}