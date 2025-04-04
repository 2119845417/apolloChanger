package com.ncf.apollodemo.pojo.model.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CardCallbackRequest {

    private String type;
    private String outTrackId;
    private String content;
    private String corpId;
    private String userId;

    @Data
    public static class ActionCallbackContent {
        private PrivateCardActionData cardPrivateData;

        @Data
        public static class PrivateCardActionData {
            private List<String> actionIds;
            private Map<String, Object> params;
        }
    }

}