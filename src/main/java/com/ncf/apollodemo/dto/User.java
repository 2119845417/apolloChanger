package com.ncf.apollodemo.dto;

import lombok.Data;

import java.util.Set;

/**
 * User类
 *
 */
@Data
public class User {
    private Integer id;

    private String userName;

    private String passWord;

    private Set<String> roles;

}

