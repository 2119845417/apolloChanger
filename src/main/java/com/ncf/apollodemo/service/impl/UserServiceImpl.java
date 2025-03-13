package com.ncf.apollodemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.dao.UserDao;
import com.ncf.apollodemo.entity.User;
import com.ncf.apollodemo.service.UserService;
import com.ncf.apollodemo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public String userLogin(String username, String password) {
        //创建一个条件构造器
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        //传入查询条件

        userQueryWrapper.eq("username", username).eq("password", password);
        User user = userDao.selectOne(userQueryWrapper);
        if (user != null) {
            String res = JWTUtils.getToken(username, user.getUserId().toString());
            return res;
        }
        return "";
    }

    @Override
    public User getOne(QueryWrapper<User> userQueryWrapper) {
        User user = userDao.selectOne(userQueryWrapper);
        return user;
    }
}
