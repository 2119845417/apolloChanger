package com.ncf.apollodemo.controller;

import com.ncf.apollodemo.manager.service.DingTalkService;
import com.ncf.apollodemo.pojo.model.request.DingTalkCreateChatRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.GroupCardInitRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.PrivateCardInitRequest;
import com.ncf.apollodemo.pojo.model.response.*;
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
    public ResponseResult<AccessTokenResponse> getAccessToken(){
        return ResponseResult.success(dingTalkService.getAccessToken());
    }

    // 根据手机号获取用户信息
    @GetMapping("/user")
    public ResponseResult<DingTalkUserResponse> getUserByMobile(
            @RequestParam String accessToken,
            @RequestParam String mobile) {
        return ResponseResult.success(dingTalkService.getUserByMobile(accessToken, mobile));
    }

    // 创建群聊
    @PostMapping("/chat-group")
    public ResponseResult<DingTalkCreateChatResponse> createChatGroup(
            @RequestParam String accessToken,
            @RequestBody DingTalkCreateChatRequest request) {
        return ResponseResult.success(dingTalkService.createChatGroup(accessToken, request));
    }

    // 获取群聊信息
    @GetMapping("/chat-group/{chatId}")
    public ResponseResult<DingTalkGetChatResponse> getChatGroupInfo(
            @RequestParam String accessToken,
            @PathVariable String chatId) {
        return ResponseResult.success(dingTalkService.getChatGroupInfo(accessToken, chatId));
    }

    // 发送群聊卡片
//    测试群会话id：cidRajsaeXdvetvkXvzWghXkg==
    @PostMapping("/group-card")
    public ResponseResult<CardInstanceResponse.Result> initGroupCard(@RequestParam String accessToken, @RequestParam String openConversationId) {
        GroupCardInitRequest request = new GroupCardInitRequest(openConversationId);
        return ResponseResult.success(dingTalkService.initGroupCard(accessToken, request));
    }

    // 发送私聊卡片
    @PostMapping("/private-card")
    public ResponseResult<CardInstanceResponse.Result> initPrivateCard(@RequestParam String accessToken,@RequestParam String userid) {
        PrivateCardInitRequest request = new PrivateCardInitRequest(userid);
        return ResponseResult.success(dingTalkService.initPrivateCard(accessToken, request));
    }
}
