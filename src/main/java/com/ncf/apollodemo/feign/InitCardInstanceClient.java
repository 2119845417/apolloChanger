package com.ncf.apollodemo.feign;

import com.ncf.apollodemo.pojo.model.request.initcard.GroupCardInitRequest;
import com.ncf.apollodemo.pojo.model.request.initcard.PrivateCardInitRequest;
import com.ncf.apollodemo.pojo.model.response.CardInstanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "initCardInstanceClient",url = "https://api.dingtalk.com")
public interface InitCardInstanceClient {

    /**
     * 群发卡片(需要互动卡片实例写权限)
     * @param accessToken 企业授权令牌
     * @param request 群发请求参数
     * @return CardInstanceResponse
     */
    @PostMapping("/v1.0/card/instances/createAndDeliver")
    CardInstanceResponse initGroupCard(
            @RequestHeader("x-acs-dingtalk-access-token") String accessToken,
            @RequestBody GroupCardInitRequest request
    );

    /**
     * 单发卡片（需要互动卡片实例写权限）
     * @param accessToken 企业授权令牌
     * @param request 单发请求参数
     * @return CardInstanceResponse
     */
    @PostMapping("/v1.0/card/instances/createAndDeliver")
    CardInstanceResponse initPrivateCard(
            @RequestHeader("x-acs-dingtalk-access-token") String accessToken,
            @RequestBody PrivateCardInitRequest request
    );

}
