package com.ncf.apollodemo.pojo.model.response;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CardCallbackResponse {

    private CardDataDTO cardData;
    private CardDataDTO userPrivateData;

    @Data
    public static class CardDataDTO {
        private Map<String, String> cardParamMap;

        public Map<String, String> getCardParamMap() {
            if (cardParamMap == null) {
                cardParamMap = new HashMap<>();
            }
            return cardParamMap;
        }

    }

}