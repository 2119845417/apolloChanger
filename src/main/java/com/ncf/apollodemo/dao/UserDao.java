package com.ncf.apollodemo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncf.apollodemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {

}
