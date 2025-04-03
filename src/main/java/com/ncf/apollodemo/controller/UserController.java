/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ncf.apollodemo.controller;

import com.ncf.apollodemo.pojo.domain.User;
import com.ncf.apollodemo.pojo.dto.UserEditInfoDTO;
import com.ncf.apollodemo.pojo.dto.UserEditInfoDTO;
import com.ncf.apollodemo.pojo.dto.UserLoginDTO;
import com.ncf.apollodemo.pojo.dto.UserLoginDTO;
import com.ncf.apollodemo.pojo.dto.UserSignDTO;
import com.ncf.apollodemo.pojo.dto.UserSignDTO;
import com.ncf.apollodemo.pojo.vo.CreateUserVO;
import com.ncf.apollodemo.resp.ResponseResult;
import com.ncf.apollodemo.manager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult<String> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        logger.info("登陆{}", userLoginDTO);
        if (userLoginDTO == null) {
            return ResponseResult.error(500,"登录失败，参数异常");
        }
        if(userLoginDTO.getUserName() == null || userLoginDTO.getPassWord() == null){
            return ResponseResult.error(500,"登录失败，账号或密码为空");
        }
        String token = userService.userLogin(userLoginDTO);
        if (token.isEmpty()) {
            return ResponseResult.error(500,"登录失败，请检查账号密码是否正确");
        }
        return ResponseResult.success(token);
    }

    @PostMapping("/signIn")
    public ResponseResult<CreateUserVO> singIn(@RequestBody UserSignDTO userSignDTO) {
        logger.info("注册{}", userSignDTO);
        if (userSignDTO == null) {
            return ResponseResult.error(500,"登录失败，参数异常");
        }
        if(userSignDTO.getUserName() == null || userSignDTO.getPassWord() == null || userSignDTO.getPhone() == null){
            return ResponseResult.error(500,"登录失败，账号、密码或手机号为空");
        }
        CreateUserVO user = userService.createUser(userSignDTO);
        if (user == null) {
            return ResponseResult.error(500,"数据库插入时异常");
        }
        return ResponseResult.success(user);
    }

    @PostMapping(value = "/editInfo")
    public ResponseResult<Boolean> editInfo(@RequestBody UserEditInfoDTO userEditInfoDTO) {
        logger.info("修改{}", userEditInfoDTO);
        if (userEditInfoDTO == null) {
            return ResponseResult.error(500,"修改失败，参数异常");
        }
        User updateUser = userService.updateUser(userEditInfoDTO);
        if (updateUser == null) {
            return ResponseResult.error(500,"更新时出现异常");
        }
        return ResponseResult.success(true);
    }

}
