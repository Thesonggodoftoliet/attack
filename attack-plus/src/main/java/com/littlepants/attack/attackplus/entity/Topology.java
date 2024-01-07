package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-05-24
 */
@Data
@TableName("att_topology")
@ApiModel(value = "Topology", description = "存储图的数据")
public class Topology implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("metd2dID")
    @TableField("meta2d_id")
    private String meta2dId;

    @TableField("topology_name")
    private String topologyName;

    @TableField("host_num")
    private Integer hostNum;

    @ApiModelProperty("nessus scanID")
    @TableField("scan_id")
    private Integer scanId;

    @ApiModelProperty("meta2djson")
    @TableField("meta2d")
    private String meta2d;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
