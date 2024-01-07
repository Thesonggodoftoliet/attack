package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 登录日志表 Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-23
 */
@Mapper
public interface LoginLogDao extends BaseMapper<LoginLog> {

}
