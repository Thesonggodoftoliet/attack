package com.littlepants.attack.attackplus.mapper;

import com.littlepants.attack.attackplus.dto.TopologyDTO;
import com.littlepants.attack.attackplus.entity.Topology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/5/24
 */
@Mapper
public interface TopologyMapper {
    TopologyMapper INSTANCE = Mappers.getMapper(TopologyMapper.class);

    @Mapping(target = "id",source ="meta2dId")
    @Mapping(target = "name",source = "topologyName")
    @Mapping(target = "num",source = "hostNum")
    @Mapping(target = "scanId",source = "scanId")
    @Mapping(target = "meta2d",source = "meta2d")
    TopologyDTO topologyToTopologyDto(Topology topology);
}
