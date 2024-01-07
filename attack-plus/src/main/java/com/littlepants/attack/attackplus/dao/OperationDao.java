package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.Operation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 对应Caldera的 operations (campaign)
 Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Mapper
public interface OperationDao extends BaseMapper<Operation> {
}
