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

import com.ncf.apollodemo.entity.User;
import com.ncf.apollodemo.resp.ResponseResult;
import com.ncf.apollodemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/login")
    public ResponseResult<String> userLogin(User user) {
        logger.info("登陆");
        String userName = user.getUserName();
        String passWord = user.getPassWord();
        String token = userService.userLogin(userName, passWord);
        if (token.isEmpty()) {
            return ResponseResult.error(500,"登录失败，请检查账号密码是否正确");
        }
        return ResponseResult.success(token);
    }
}
