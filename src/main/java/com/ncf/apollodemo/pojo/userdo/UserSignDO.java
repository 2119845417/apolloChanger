package com.ncf.apollodemo.pojo.userdo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignDO {

    private String userName;

    private String passWord;

    private String phone;
}
