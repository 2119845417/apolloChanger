package com.ncf.apollodemo.pojo.userdo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditInfoDO {

    private Long id;

    private String name;

    private int department;

    private String phone;
}
