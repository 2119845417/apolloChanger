package com.ncf.apollodemo.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ncf.apollodemo.config.SecretConstant;
import com.ncf.apollodemo.dao.UserDao;
import com.ncf.apollodemo.manager.service.UserService;
import com.ncf.apollodemo.pojo.domain.User;
import com.ncf.apollodemo.pojo.dto.UserEditInfoDTO;
import com.ncf.apollodemo.pojo.dto.UserEditInfoDTO;
import com.ncf.apollodemo.pojo.dto.UserLoginDTO;
import com.ncf.apollodemo.pojo.dto.UserLoginDTO;
import com.ncf.apollodemo.pojo.dto.UserSignDTO;
import com.ncf.apollodemo.pojo.dto.UserSignDTO;
import com.ncf.apollodemo.pojo.vo.CreateUserVO;
import com.ncf.apollodemo.utils.JWTUtils;
import com.ncf.apollodemo.utils.Md5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SecretConstant secretConstant;

    @Override
    public String userLogin(UserLoginDTO userLoginDO) {
        //创建一个条件构造器
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        //传入查询条件
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
    public CreateUserVO createUser(UserSignDTO userSignDO) {
        QueryWrapper<User> userName1 = new QueryWrapper<User>().eq("user_name", userSignDO.getUserName());
        User existUser = getOne(userName1);
        // 1. 用户查重
        if (existUser != null) {
            throw new ApplicationContextException("用户已存在");
        }
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
            QueryWrapper<User> userName2 = new QueryWrapper<User>().eq("user_name", userSignDO.getUserName());
            User createUser = getOne(userName2);
            CreateUserVO createUserVO = new CreateUserVO();
            BeanUtils.copyProperties(createUser, createUserVO);
            // 4. 生成脱敏响应
            return createUserVO;
        }else {
            throw new ApplicationContextException("插入数据库失败");
        }

    }

    @Override
    public User updateUser(UserEditInfoDTO userEditInfoDO) {
        User oldUser = userDao.selectById(userEditInfoDO.getId());
        //todo
        return null;
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
