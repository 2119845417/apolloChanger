package com.ncf.apollodemo.pojo.dto;

import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ncf.apollodemo.pojo.domain.AddXxlJob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateDTO {
    private String opsUser;
    private OpenItemDTO openItemDTO;
    private String phone;
    private String reason;
    private AddXxlJob addXxlJob;
}
