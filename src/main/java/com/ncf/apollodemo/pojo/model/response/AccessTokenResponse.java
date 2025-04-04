package com.ncf.apollodemo.pojo.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *获取企业内部应用的access_token API返回对象
 */
@Data
public class AccessTokenResponse {
    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("access_token")
    private String accessToken; //生成的accessToken。
    @JsonProperty("errmsg")
    private String errmsg;
    @JsonProperty("expires_in")
    private Long expiresIn; //accessToken的过期时间，单位秒。
}
