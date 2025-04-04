package com.ncf.apollodemo.controller;

import com.ncf.apollodemo.dingtalkservice.DingTalkService;
import com.ncf.apollodemo.pojo.model.request.DingTalkCreateChatRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.GroupCardInitRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.PrivateCardInitRequest;
import com.ncf.apollodemo.resp.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注意：本层仅用于测试使用，不建议将access-token等关键信息返回到前端！！！！
 */
@RestController
@RequestMapping("/api/dingtalk")
public class DingTalkController {

    @Autowired
    private DingTalkService dingTalkService;

    // 获取访问令牌
    @GetMapping("/access-token")
    public ResponseResult getAccessToken(){
        return dingTalkService.getAccessToken();
    }

    // 根据手机号获取用户信息
    @GetMapping("/user")
    public ResponseResult getUserByMobile(
            @RequestParam String accessToken,
            @RequestParam String mobile) {
        return dingTalkService.getUserByMobile(accessToken, mobile);
    }

    // 创建群聊
    @PostMapping("/chat-group")
    public ResponseResult createChatGroup(
            @RequestParam String accessToken,
            @RequestBody DingTalkCreateChatRequest request) {
        return dingTalkService.createChatGroup(accessToken, request);
    }

    // 获取群聊信息
    @GetMapping("/chat-group/{chatId}")
    public ResponseResult getChatGroupInfo(
            @RequestParam String accessToken,
            @PathVariable String chatId) {
        return dingTalkService.getChatGroupInfo(accessToken, chatId);
    }

    // 发送群聊卡片
    @PostMapping("/group-card")
    public ResponseResult initGroupCard(@RequestParam String accessToken,@RequestParam String openConversationId) {
        GroupCardInitRequest request = new GroupCardInitRequest(openConversationId);
        return dingTalkService.initGroupCard(accessToken, request);
    }

    // 发送私聊卡片
    @PostMapping("/private-card")
    public ResponseResult initPrivateCard(@RequestParam String accessToken,@RequestParam String userid) {
        PrivateCardInitRequest request = new PrivateCardInitRequest(userid);
        return dingTalkService.initPrivateCard(accessToken, request);
    }
}
