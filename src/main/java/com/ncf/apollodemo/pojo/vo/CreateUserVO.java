package com.ncf.apollodemo.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserVO {

    private Long id;

    private Long userId;

    private String userName;
}
