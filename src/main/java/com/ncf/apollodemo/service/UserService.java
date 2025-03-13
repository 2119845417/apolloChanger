package com.ncf.apollodemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.entity.User;

public interface UserService {

    String userLogin(String username, String password);
    User getOne(QueryWrapper<User> userQueryWrapper);
}
