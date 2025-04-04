package com.ncf.apollodemo.dingtalkservice.impl;

import com.ncf.apollodemo.dingtalkservice.DingTalkService;
import com.ncf.apollodemo.feign.DingTalkClient;
import com.ncf.apollodemo.feign.InitCardInstanceClient;
import com.ncf.apollodemo.pojo.model.request.DingTalkCreateChatRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.GroupCardInitRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.PrivateCardInitRequest;
import com.ncf.apollodemo.pojo.model.response.*;
import com.ncf.apollodemo.resp.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class DingTalkServiceImpl implements DingTalkService {
    @Autowired
    private DingTalkClient dingTalkClient;
    @Autowired
    private InitCardInstanceClient initCardInstanceClient;

    @Value("${dingtalk.appKey}")
    private String APPKEY;
    @Value("${dingtalk.appSecret}")
    private String APPSECRET;
    @Value("${dingtalk.cardTemplateId}")
    private String CARDTEMPLATEID;
    @Value("${dingtalk.robotCode}")
    private String ROBOTCODE;
    @Value("${dingtalk.callbackType}")
    private String CALLBACKTYPE;


    @Override
    public ResponseResult getAccessToken() {
        AccessTokenResponse accessTokenObj = dingTalkClient.getAccessToken(APPKEY, APPSECRET);
        return ResponseResult.success(accessTokenObj);
    }

    @Override
    public ResponseResult getUserByMobile(String accessToken, String mobile) {
        DingTalkUserResponse userByMobile = dingTalkClient.getUserByMobile(accessToken, mobile);
        return ResponseResult.success(userByMobile.getResult());
    }

    @Override
    public ResponseResult createChatGroup(String accessToken, DingTalkCreateChatRequest request) {
        //TODO
        /**
         * 对request做出个性化，要注意一下几点：
         * name: 群名称：群的名称，建议包含主题或用途，便于后续管理。
         * owner: 群主的用户 ID：该员工必须为会话 `useridlist` 的成员之一。
         * useridlist: 群成员的用户 ID 列表：群成员列表，每次最多支持 40 人，群人数上限为 1000。可通过根据手机号查询用户接口获取 `userid` 参数值。
         * showHistoryType: 是否显示历史消息：新成员是否可查看 100 条历史消息。1：可查看，0：不可查看，默认为不可查看。
         * searchable: 是否可搜索群：群是否可以被搜索。0（默认）：不可搜索，1：可搜索。
         * validationType: 入群是否需要验证：入群是否需要验证。0（默认）：不验证，1：入群验证。
         * mentionAllAuthority: @全体成员的权限：@all 使用范围。0（默认）：所有人可使用，1：仅群主可 @all。
         * managementType: 群管理类型：群管理类型。0（默认）：所有人可管理，1：仅群主可管理。
         * chatBannedType: 是否开启群禁言：是否开启群禁言。0（默认）：不禁言，1：全员禁言。
         */
        DingTalkCreateChatResponse chatGroupInfo = dingTalkClient.createChatGroup(accessToken, request);
        return ResponseResult.success(chatGroupInfo);
        /**
         * 注意：
         * 返回中拿到的"chatid"与"openConversationId"非常重要
         * 是卡片回调的重要参数
         */
    }

    @Override
    public ResponseResult getChatGroupInfo(String accessToken, String chatId) {
        DingTalkGetChatResponse chatGroupInfo = dingTalkClient.getChatGroupInfo(accessToken, chatId);
        return ResponseResult.success(chatGroupInfo.getChatInfo());
    }

    @Override
    public ResponseResult initGroupCard(String accessToken, GroupCardInitRequest request) {
        // 填充静态数据
        request.setCardTemplateId(CARDTEMPLATEID);//模板id
        request.setCallbackType(CALLBACKTYPE);//卡片回调类型
        // 填充 imGroupOpenDeliverModel（群聊机器人ID）
        GroupCardInitRequest.GroupDeliverModel groupDeliverModel = new GroupCardInitRequest.GroupDeliverModel(ROBOTCODE);
        request.setImGroupOpenDeliverModel(groupDeliverModel);


        //TODO
        // 下方数据根据自己来填入完善
        request.setOutTrackId(UUID.randomUUID().toString());//发送的这张卡片的唯一ID
        // 填充 cardData
        GroupCardInitRequest.CardData cardData = new GroupCardInitRequest.CardData();
        GroupCardInitRequest.CardParamMap cardParamMap = new GroupCardInitRequest.CardParamMap();
        cardParamMap.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        cardParamMap.setTitle("群发测试提交的申请标题Title");
        cardParamMap.setType("群发测试Type");
        cardParamMap.setReason("群发测试Reason");
        cardParamMap.setLastMessage("群发测试LastMessage");
        cardParamMap.setStatus("");
        cardData.setCardParamMap(cardParamMap);
        request.setCardData(cardData);


        CardInstanceResponse cardInstanceResponse = initCardInstanceClient.initGroupCard(accessToken, request);
        return ResponseResult.success(cardInstanceResponse.getResult());
    }

    @Override
    public ResponseResult initPrivateCard(String accessToken, PrivateCardInitRequest request) {
        // 填充静态数据
        request.setCardTemplateId("3b052083-d23e-493c-bbd5-f000a46fe668.schema");
        request.setCallbackType(CALLBACKTYPE);


        //TODO
        // 下方数据根据自己来填入完善
        request.setOutTrackId(UUID.randomUUID().toString());//卡片的唯一ID,
        // 填充 cardData
        PrivateCardInitRequest.CardData cardData = new PrivateCardInitRequest.CardData();
        PrivateCardInitRequest.CardParamMap cardParamMap = new PrivateCardInitRequest.CardParamMap();
        cardParamMap.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        cardParamMap.setTitle("私发测试提交的申请标题Title");
        cardParamMap.setType("私发测试Type");
        cardParamMap.setReason("私发测试Reason");
        cardParamMap.setLastMessage("私发测试LastMessage");
        cardParamMap.setStatus("");
        cardData.setCardParamMap(cardParamMap);
        request.setCardData(cardData);


        CardInstanceResponse cardInstanceResponse = initCardInstanceClient.initPrivateCard(accessToken, request);
        return ResponseResult.success(cardInstanceResponse.getResult());
    }


}
