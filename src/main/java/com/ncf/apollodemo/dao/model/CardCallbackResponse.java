package com.ncf.apollodemo.dao.model;


import java.util.HashMap;
import java.util.Map;

public class CardCallbackResponse {

    private CardDataDTO cardData;
    private CardDataDTO userPrivateData;

    public static class CardDataDTO {
        private Map<String, String> cardParamMap;

        public Map<String, String> getCardParamMap() {
            if (cardParamMap == null) {
                cardParamMap = new HashMap<>();
            }
            return cardParamMap;
        }

        public void setCardParamMap(Map<String, String> cardParamMap) {
            this.cardParamMap = cardParamMap;
        }
    }

    public CardDataDTO getCardData() {
        return cardData;
    }

    public void setCardData(CardDataDTO cardData) {
        this.cardData = cardData;
    }

    public CardDataDTO getUserPrivateData() {
        return userPrivateData;
    }

    public void setUserPrivateData(CardDataDTO userPrivateData) {
        this.userPrivateData = userPrivateData;
    }
}