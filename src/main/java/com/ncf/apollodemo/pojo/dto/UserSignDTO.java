package com.ncf.apollodemo.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignDTO {

    private String userName;

    private String passWord;

    private String phone;
}
