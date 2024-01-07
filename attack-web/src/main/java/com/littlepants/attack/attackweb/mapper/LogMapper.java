package com.littlepants.attack.attackweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackweb.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<AccessLog> {
}
