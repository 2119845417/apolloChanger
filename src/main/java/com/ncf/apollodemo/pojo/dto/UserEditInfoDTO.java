package com.ncf.apollodemo.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditInfoDTO {

    private Long id;

    private String name;

    private int department;

    private String phone;
}
