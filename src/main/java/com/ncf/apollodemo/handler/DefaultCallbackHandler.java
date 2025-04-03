package com.ncf.apollodemo.handler;

import com.ncf.apollodemo.pojo.model.CardCallbackRequest;
import com.ncf.apollodemo.pojo.model.CardCallbackResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 默认回调处理器，用于处理钉钉回调请求并返回响应，
 * 使用时可将@Component注解删除，然后二次实现CustomCallbackHandler，进行个性化
 */
@Component
public class DefaultCallbackHandler implements CustomCallbackHandler {

    private static final Logger log = LogManager.getLogger(DefaultCallbackHandler.class);
    private static final String AGREE_ACTION = "agree";
    private static final String REGECT_ACTION = "reject";

    /**
     * 处理钉钉回调请求的主方法。
     * 根据回调参数执行业务逻辑，并构造响应对象。
     *
     * @param request 钉钉回调请求对象，内含卡片的唯一ID，点击按钮人的UserId关键数据
     * @param params  回调参数
     * @return 响应对象
     */
    @Override
    public CardCallbackResponse handleCallback(CardCallbackRequest request, Map<String, Object> params) {
        log.info("默认回调处理器处理回调请求");
        log.info("回调卡片ID: {}", request.getOutTrackId());
        log.info("回调用户ID: {}", request.getUserId());
        //TODO,可以根据卡片id,用户id来决定返回卡片的的数据(因为无法单独更新按钮，整个卡片是一起更新的)

        // 根据按钮动作处理回调逻辑
        String newStatus = handleCallbackLogic(params);
        // 构造响应对象
        CardCallbackResponse response = new CardCallbackResponse();
        response.setCardData(updateCardData(newStatus));//你可以自定义响应内容
        response.setUserPrivateData(updateUserPrivateData(newStatus));

        log.info("回调处理完成，返回响应对象");
        return response;
    }

    /**
     * 处理回调逻辑，根据传入的按钮参数决定回调的处理结果。
     * 即判断点击的是同意还是拒绝
     *
     * @param params 回调参数
     * @return 回调处理结果
     */
    private String handleCallbackLogic(Map<String, Object> params) {
        if (params != null && params.containsKey("action")) {
            String action = params.get("action").toString();
            log.info("处理参数action: {}", action);
            // 根据action参数执行不同业务逻辑
            if (AGREE_ACTION.equals(action)) {
                log.info("审批通过，参数: {}", params);
                return AGREE_ACTION;
            } else if (REGECT_ACTION.equals(action)) {
                log.info("审批拒绝，参数: {}", params);
                return REGECT_ACTION;
            }
        }
        return "";//出现错误，返回空，确保最重要的状态不会发送改变
    }

    /**
     * 更新卡片数据，根据传入的状态更新卡片的参数。
     *
     * @param status 卡片状态
     * @return 更新后的卡片数据
     */
    private CardCallbackResponse.CardDataDTO updateCardData(String status) {
        CardCallbackResponse.CardDataDTO cardData = new CardCallbackResponse.CardDataDTO();

        // 确保 cardParamMap 即使要返回给卡片的数据，更新卡盘状态，
        // 这里你可以个性化定义
        cardData.getCardParamMap().put("status", status);
        cardData.getCardParamMap().put("title", "牛灿菲提交的配置更改申请按钮回复测试");
        cardData.getCardParamMap().put("createTime", "牛牛时间回复测试");
        cardData.getCardParamMap().put("type", "配置文件回复测试");
        cardData.getCardParamMap().put("reason", "请求审批回复测试");
        cardData.getCardParamMap().put("lastMessage", "审批回复测试");

        log.info("更新卡片状态为: {}", status);
        return cardData;
    }

    /**
     * 更新用户私有数据，根据传入的状态更新用户的私有参数。
     * 目前我们没有私有数据，所以无用
     *
     * @param status 用户状态
     * @return 更新后的用户私有数据
     */
    private CardCallbackResponse.CardDataDTO updateUserPrivateData(String status) {
        CardCallbackResponse.CardDataDTO userPrivateData = new CardCallbackResponse.CardDataDTO();

        // cardParamMap为更新后的私有数据
        userPrivateData.getCardParamMap().put("userStatus", status);
        log.info("更新用户私有状态为: {}", status);

        return userPrivateData;
    }
}