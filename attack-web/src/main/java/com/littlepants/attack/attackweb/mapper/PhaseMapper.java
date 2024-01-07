package com.littlepants.attack.attackweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackweb.entity.Phase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PhaseMapper extends BaseMapper<Phase> {
    @Select("SELECT phase_name FROM attack_phase WHERE id IN(#{ids})")
    List<String> selectNameBatchId(@Param("ids")String ids);
}
