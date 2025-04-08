package com.ncf.apollodemo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncf.apollodemo.pojo.domain.ApolloClientConfig;
import com.ncf.apollodemo.pojo.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApolloClientConfigDao extends BaseMapper<ApolloClientConfig> {
}
