package com.ncf.apollodemo.feign;

import com.ncf.apollodemo.pojo.model.request.DingTalkCreateChatRequest;
import com.ncf.apollodemo.pojo.model.response.AccessTokenResponse;
import com.ncf.apollodemo.pojo.model.response.DingTalkCreateChatResponse;
import com.ncf.apollodemo.pojo.model.response.DingTalkGetChatResponse;
import com.ncf.apollodemo.pojo.model.response.DingTalkUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 钉钉API请求
 */
@FeignClient(name = "dingTalkClient",url = "https://oapi.dingtalk.com")
public interface DingTalkClient {

    /**
     * 获取企业内部应用的access_token(权限默认开通，无需申请)
     * @param appKey 已创建的企业内部应用的AppKey。
     * @param appSecret 已创建的企业内部应用的AppSecret。
     * @return AccessTokenResponse
     */
    @GetMapping("/gettoken")
    AccessTokenResponse getAccessToken(
            @RequestParam("appkey") String appKey,
            @RequestParam("appsecret") String appSecret
    );

    /**
     * 根据手机号查询用户（需开通根据手机号获取成员基本信息权限）
     * @param accessToken 调用该接口的应用凭证。
     * @param mobile 手机号
     * @return DingTalkUserResponse
     */
    @PostMapping("/topapi/v2/user/getbymobile")
    DingTalkUserResponse getUserByMobile(
            @RequestParam("access_token") String accessToken,
            @RequestPart("mobile") String mobile
    );

    /**
     * 创建群（需开通钉钉群基础信息管理权限）
     * @param accessToken 调用该接口的应用凭证。
     * @param request 创建群聊请求体（字段详情查看DingTalkCreateChatRequest类或下方）
     * @return DingTalkCreateChatResponse
     *
*    * DingTalkCreateChatRequest要注意一下几点：
*    * name: 群名称：群的名称，建议包含主题或用途，便于后续管理。
*    * owner: 群主的用户 ID：该员工必须为会话 `useridlist` 的成员之一。
*    * useridlist: 群成员的用户 ID 列表：群成员列表，每次最多支持 40 人，群人数上限为 1000。可通过根据手机号查询用户接口获取 `userid` 参数值。
*    * showHistoryType: 是否显示历史消息：新成员是否可查看 100 条历史消息。1：可查看，0：不可查看，默认为不可查看。
*    * searchable: 是否可搜索群：群是否可以被搜索。0（默认）：不可搜索，1：可搜索。
*    * validationType: 入群是否需要验证：入群是否需要验证。0（默认）：不验证，1：入群验证。
*    * mentionAllAuthority: @全体成员的权限：@all 使用范围。0（默认）：所有人可使用，1：仅群主可 @all。      * managementType: 群管理类型：群管理类型。0（默认）：所有人可管理，1：仅群主可管理。
*    * chatBannedType: 是否开启群禁言：是否开启群禁言。0（默认）：不禁言，1：全员禁言。
     *
     */
    @PostMapping(value = "/chat/create")
    DingTalkCreateChatResponse createChatGroup(
            @RequestParam("access_token") String accessToken,
            @RequestBody DingTalkCreateChatRequest request
    );

    /**
     * 获取钉钉群聊信息（需开启钉钉群基础信息读权限）
     * @param accessToken 调用该接口的应用凭证。
     * @param chatId 群会话的ID（仅支持通过调用服务端创建群接口获取的chatid参数值）
     * @return DingTalkGetChatResponse
     */
    @GetMapping(value = "/chat/get")
    DingTalkGetChatResponse getChatGroupInfo(
            @RequestParam("access_token") String accessToken,
            @RequestParam("chatid") String chatId
    );



}
