package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.Testcase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/28
 */

@Mapper
public interface TestcaseDao extends BaseMapper<Testcase> {
    @Select("SELECT * FROM att_testcase_view LIMIT #{current},#{count}")
    List<Testcase> getListPage(@Param("current")int current,@Param("count")int count);
}
