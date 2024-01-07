package com.littlepants.attack.attackplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.littlepants.attack.attackplus.entity.Host;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 每个场景包含的IP Mapper 接口
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Mapper
public interface HostDao extends BaseMapper<Host> {
    @Select({
            "<script>",
                "SELECT id FROM att_host WHERE ip IN",
                "<foreach collection='ips' item='item' open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>"
    })
    List<Long> getIdsByIps(@Param("ips") List<String> ips);

    @Select({
            "<script>",
                "SELECT paw FROM att_host WHERE id IN",
                "<foreach collection='ids' item='item' open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>"
    })
    List<String> getPawsByIds(@Param("ids") List<Long> ids);

    @Select("SELECT * FROM att_host WHERE topology_id = #{topology_id}")
    List<Host> getHostsByTopology(@Param("topology_id")Long topologyId);

    @Select("SELECT * FROM att_host WHERE id in " +
            "(SELECT ip_id FROM att_operation_ip WHERE operation_id= #{operation_id})")
    List<Host> getHostsByOperation(@Param("operation_id")Long operationId);
}
