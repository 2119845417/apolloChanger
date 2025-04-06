package com.ncf.apollodemo.pojo.dto;

import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallBackDTO {
    private String appId;
    private String env;
    private CreateOrUpdateDTO createOrUpdateDTO;
}
