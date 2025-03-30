package com.ncf.apollodemo.handler;
import com.ncf.apollodemo.dao.model.CardCallbackRequest;
import com.ncf.apollodemo.dao.model.CardCallbackResponse;

import java.util.Map;

/**
 * 回调处理器接口
 */
public interface CustomCallbackHandler {
    CardCallbackResponse handleCallback(CardCallbackRequest request, Map<String, Object> params);
}