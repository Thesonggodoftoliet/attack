package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-21
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}
