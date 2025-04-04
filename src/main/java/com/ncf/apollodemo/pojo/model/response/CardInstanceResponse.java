package com.ncf.apollodemo.pojo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardInstanceResponse {
    private Boolean success; // 是否成功

    @JsonProperty("result")
    private Result result; // 结果

    @Data
    public static class Result {
        @JsonProperty("deliverResults")
        private DeliverResults[] deliverResults; // 交付结果

        private String outTrackId; // 外部跟踪 ID

        @Data
        public static class DeliverResults {
            private String spaceId; // 空间 ID
            private String spaceType; // 空间类型
            private Boolean success; // 是否成功
            private String carrierId; // 载体 ID
        }
    }
}