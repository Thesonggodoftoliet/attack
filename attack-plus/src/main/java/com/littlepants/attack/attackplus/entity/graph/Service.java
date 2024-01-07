package com.littlepants.attack.attackplus.entity.graph;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since  2022/12/5
 */
@Data
@Node("Service")
public class Service {
    @Id
    @GeneratedValue
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String vendor;
    private Double value;
    private String description;
    @Property("plugin_id")
    private String pluginId;
}
