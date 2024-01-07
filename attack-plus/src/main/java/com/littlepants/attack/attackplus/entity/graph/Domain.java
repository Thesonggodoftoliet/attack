package com.littlepants.attack.attackplus.entity.graph;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * <p>
 * 域
 * neo4j 设置实体时，关系不能设置双向，否则在查询时会出现循环引用的问题
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/26
 */

@Data
@Node("Domain")
public class Domain {
    @Id
    private String id;
    private String name;
    private String description;
}
