package com.ncf.apollodemo.listener;

import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.ncf.apollodemo.dao.model.CardCallbackRequest;
import com.ncf.apollodemo.dao.model.CardCallbackResponse;
import com.ncf.apollodemo.handler.CustomCallbackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shade.com.alibaba.fastjson2.JSON;

import java.util.Map;

/**
 * 该类为监听器，建立连接长连接后一直监听请求，进行解析请求内容
 */
@Component
public class DingTalkCallbackListener implements OpenDingTalkCallbackListener<CardCallbackRequest, CardCallbackResponse> {

    private static final Logger log = LogManager.getLogger(DingTalkCallbackListener.class);

    @Autowired
    private CustomCallbackHandler customCallbackHandler;

    @Override
    public CardCallbackResponse execute(CardCallbackRequest request) {
        log.info("接收到钉钉回调请求，正在处理...");

        // 打印接收到的请求内容
        log.info("回调类型: {}", request.getType());
        log.info("卡片ID: {}", request.getOutTrackId());
        log.info("企业ID: {}", request.getCorpId());
        log.info("用户ID: {}", request.getUserId());
        log.info("回调内容: {}", request.getContent());

        // 解析回调内容
        CardCallbackRequest.ActionCallbackContent actionCallbackContent = JSON.parseObject(request.getContent(), CardCallbackRequest.ActionCallbackContent.class);
        CardCallbackRequest.ActionCallbackContent.PrivateCardActionData privateCardActionData = actionCallbackContent.getCardPrivateData();

        // 获取按钮参数
        Map<String, Object> params = privateCardActionData.getParams();

        // 打印按钮参数
        log.info("按钮参数: {}", params);

        // 调用自定义回调处理器，返回的为请求参数，与处理类型
        // 注意，request中含有卡片的唯一ID(OutTrackId)，与点击按钮人的UserId,可作为后续业务逻辑的关键切入点
        return customCallbackHandler.handleCallback(request, params);
    }
}