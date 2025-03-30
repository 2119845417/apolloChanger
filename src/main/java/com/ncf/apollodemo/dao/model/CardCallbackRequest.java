package com.ncf.apollodemo.dao.model;

import java.util.List;
import java.util.Map;

public class CardCallbackRequest {

    private String type;
    private String outTrackId;
    private String content;
    private String corpId;
    private String userId;

    public static class ActionCallbackContent {
        private PrivateCardActionData cardPrivateData;

        public static class PrivateCardActionData {
            private List<String> actionIds;
            private Map<String, Object> params;

            public List<String> getActionIds() {
                return actionIds;
            }

            public void setActionIds(List<String> actionIds) {
                this.actionIds = actionIds;
            }

            public Map<String, Object> getParams() {
                return params;
            }

            public void setParams(Map<String, Object> params) {
                this.params = params;
            }
        }

        public PrivateCardActionData getCardPrivateData() {
            return cardPrivateData;
        }

        public void setCardPrivateData(PrivateCardActionData cardPrivateData) {
            this.cardPrivateData = cardPrivateData;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOutTrackId() {
        return outTrackId;
    }

    public void setOutTrackId(String outTrackId) {
        this.outTrackId = outTrackId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}