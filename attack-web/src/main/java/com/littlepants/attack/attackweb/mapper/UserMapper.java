package com.littlepants.attack.attackweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackweb.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
