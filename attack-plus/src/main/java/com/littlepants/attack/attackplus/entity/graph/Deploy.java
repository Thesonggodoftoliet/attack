package com.littlepants.attack.attackplus.entity.graph;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/3
 */

@Data
@RelationshipProperties
public class Deploy {
    @RelationshipId
    private final Long id;
    private final String version;
    private final List<String> ports;
    @TargetNode
    private final Service service;

}