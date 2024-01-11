package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.Case;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Mapper
public interface CaseDao extends BaseMapper<Case> {
    @Select("SELECT id FROM att_case WHERE operation_id = #{operation_id} " +
            "AND testcase_id IN (SELECT id FROM att_testcase_caldera WHERE ability_id = #{ability_id})")
    List<Long> getIdsByOperationAndAbility(@Param("operation_id")Long operationId,@Param("ability_id")String abilityId);

    @Select("SELECT id FROM att_case WHERE operation_id = #{operation_id}")
    List<Long> getIdsByOperationId(@Param("operation_id")Long operationId);

    @Select("SELECT * FROM att_case WHERE operation_id = #{operation_id}")
    List<Case> getCasesByOperationId(@Param("operation_id")Long operationId);

    @Select("SELECT state AS progress, count(state) AS value FROM att_case WHERE operation_id=#{operation_id} " +
            "GROUP BY state")
    List<Map<String,Object>> getProgressByOperationId(@Param("operation_id")Long operationId);

    @Select("SELECT outcome, count(outcome) AS value FROM att_case WHERE operation_id=#{operation_id} GROUP BY outcome")
    List<Map<String,Object>> getOutcomeByOperationId(@Param("operation_id")Long operationId);
}
