package com.ncf.apollodemo.service.impl;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.config.SecretConstant;
import com.ncf.apollodemo.dao.UserDao;
import com.ncf.apollodemo.pojo.entity.User;
import com.ncf.apollodemo.pojo.userdo.UserLoginDO;
import com.ncf.apollodemo.pojo.userdo.UserSignDO;
import com.ncf.apollodemo.pojo.vo.CreateUserVO;
import com.ncf.apollodemo.service.UserService;
import com.ncf.apollodemo.utils.JWTUtils;
import com.ncf.apollodemo.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SecretConstant secretConstant;

    @Override
    public String userLogin(UserLoginDO userLoginDO) {
        //创建一个条件构造器
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        //传入查询条件
//        RSA rsa = secretConstant.getRSA();
//        // 2. 密码加密
//        String encryptedPwd = rsa.encryptBase64(userLoginDO.getPassWord(), KeyType.PublicKey);
        String encryptedPwd = Md5Utils.code(userLoginDO.getPassWord());
        userQueryWrapper.eq("user_name", userLoginDO.getUserName()).eq("password", encryptedPwd);

        User user = getOne(userQueryWrapper);
        if (user != null) {
            String res = JWTUtils.getToken(user.getUserName(), user.getUserId().toString());
            return res;
        }
        return "";
    }

    @Override
    public CreateUserVO createUser(UserSignDO userSignDO) {
        QueryWrapper<User> userName1 = new QueryWrapper<User>().eq("user_name", userSignDO.getUserName());
        User existUser = getOne(userName1);
        // 1. 用户查重
        if (existUser != null) {
            throw new ApplicationContextException("用户已存在");
        }
//        RSA rsa = secretConstant.getRSA();
        // 2. 密码加密
//        RSA rsa = new RSA(secretConstant.PRIVATE_KEY, secretConstant.PUBLIC_KEY);
        String encryptedPwd = Md5Utils.code(userSignDO.getPassWord());

        User newUser = new User();
        Long userId = generateUserId();
        newUser.setUserId(userId);
        newUser.setUserName(userSignDO.getUserName());
        newUser.setPassWord(encryptedPwd);
        newUser.setPhone(userSignDO.getPhone());

        // 3. 保存用户
        int newUserRes = userDao.insert(newUser);
        if(newUserRes > 0){
            QueryWrapper<User> userName2 = new QueryWrapper<User>().eq("userName", userSignDO.getUserName());
            User createUser = getOne(userName2);
            CreateUserVO createUserVO = new CreateUserVO();
            createUserVO.setId(createUser.getId());
            createUserVO.setUserId(createUser.getUserId());
            createUserVO.setUserName(createUser.getUserName());
            // 4. 生成脱敏响应
            return createUserVO;
        }else {
            throw new ApplicationContextException("插入数据库失败");
        }

    }

    @Override
    public User getOne(QueryWrapper<User> userQueryWrapper) {
        User user = userDao.selectOne(userQueryWrapper);
        return user;
    }
    private Long generateUserId(){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        List<User> users = userDao.selectList(userQueryWrapper);
        if(CollectionUtils.isEmpty(users)){
            return new Long("10001");
        }
        Long max = users.get(0).getUserId();
        for (User user : users) {
            max = Math.max(max,user.getUserId());
        }
        return max+1;
    }
}
