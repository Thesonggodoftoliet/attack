package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.CaseTools;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
@Mapper
public interface CaseToolsDao extends BaseMapper<CaseTools> {
    @Delete("DELETE FROM att_case_tools WHERE case_id = #{case_id}")
    int deleteBatchByCaseId(@Param("case_id")Long caseId);

    @Delete("DELETE FROM att_case_tools WHERE tools_id = #{tool_id}")
    int deleteBatchByToolId(@Param("tool_id")Long toolId);
}
