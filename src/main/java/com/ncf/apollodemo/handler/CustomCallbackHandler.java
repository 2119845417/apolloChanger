package com.ncf.apollodemo.handler;
import com.ncf.apollodemo.pojo.model.CardCallbackRequest;
import com.ncf.apollodemo.pojo.model.CardCallbackResponse;

import java.util.Map;

/**
 * 回调处理器接口
 */
public interface CustomCallbackHandler {
    CardCallbackResponse handleCallback(CardCallbackRequest request, Map<String, Object> params);
}