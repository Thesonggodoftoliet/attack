package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.Timeline;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Mapper
public interface TimelineDao extends BaseMapper<Timeline> {
    @Delete("DELETE FROM att_timeline WHERE case_id = #{case_id}")
    void deleteBatchByCaseId(@Param("case_id")Long caseId);

    @Select("SELECT * FROM att_time WHERE case_id = #{case_id} ORDER BY timeline DESC")
    List<Timeline> getTimelinesByCase(@Param("case_id")Long caseId);
}
