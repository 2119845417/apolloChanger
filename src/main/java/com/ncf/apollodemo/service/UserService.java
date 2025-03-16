package com.ncf.apollodemo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.pojo.entity.User;
import com.ncf.apollodemo.pojo.userdo.UserEditInfoDO;
import com.ncf.apollodemo.pojo.userdo.UserLoginDO;
import com.ncf.apollodemo.pojo.userdo.UserSignDO;
import com.ncf.apollodemo.pojo.vo.CreateUserVO;

public interface UserService {

    String userLogin(UserLoginDO userLoginDO);
    CreateUserVO createUser(UserSignDO userSignDO);
    User updateUser(UserEditInfoDO userEditInfoDO);
    User getOne(QueryWrapper<User> userQueryWrapper);
}
