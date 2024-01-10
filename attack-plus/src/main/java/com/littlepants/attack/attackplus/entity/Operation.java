package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 对应Caldera的 operations (campaign)

 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Data
@TableName("att_operation")
@ApiModel(value = "Operation", description = "对应Caldera的 operations (campaign) ")
public class Operation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("已创建的方案模板")
    @TableField("campaign_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long campaignId;

    @ApiModelProperty("0 未开始 1 进行中 2 已完成")
    @TableField("state")
    private Integer state;

    @TableField("start_time")
    private LocalDateTime startTime;

    @TableField("operation_name")
    private String operationName;

    @TableField("operation_id")
    private String operationId;
}
