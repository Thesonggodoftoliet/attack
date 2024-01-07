package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.Tools;
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
public interface ToolsDao extends BaseMapper<Tools> {
    @Select("SELECT tool_vendor AS vendor, COUNT(*) AS num FROM att_tools GROUP BY tool_vendor")
    List<Map<String,Object>> getVendors();

    @Select("SELECT * FROM att_tools WHERE tool_vendor LIKE %#{tool_vendor}%")
    List<Tools> getToolsByVendor(@Param("tool_vendor") String vendorName);
}
