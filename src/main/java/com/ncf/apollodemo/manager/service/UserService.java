package com.ncf.apollodemo.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.pojo.domain.User;
import com.ncf.apollodemo.pojo.dto.UserEditInfoDTO;
import com.ncf.apollodemo.pojo.dto.UserLoginDTO;
import com.ncf.apollodemo.pojo.dto.UserSignDTO;
import com.ncf.apollodemo.pojo.vo.CreateUserVO;

public interface UserService {

    String userLogin(UserLoginDTO userLoginDO);
    CreateUserVO createUser(UserSignDTO userSignDO);
    User updateUser(UserEditInfoDTO userEditInfoDO);
    User getOne(QueryWrapper<User> userQueryWrapper);
}
