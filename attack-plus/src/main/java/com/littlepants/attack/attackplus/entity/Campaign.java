package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 测试用例的顺序等
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Data
@TableName("att_campaign")
@ApiModel(value = "Campaign", description = "测试用例的顺序等")
public class Campaign implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("campaign_name")
    private String campaignName;

    @TableField("campaign_description")
    private String campaignDescription;

    @TableField("adversary_id")
    private String adversaryId;
}
