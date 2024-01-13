package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serial;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2024-01-13
 */
@Data
@TableName("att_tech")
@ApiModel(value = "Tech", description = "")
public class Tech implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("att_ck_id")
    private String attCkId;

    @TableField("name_en")
    private String nameEn;

    @TableField("description_en")
    private String descriptionEn;

    @TableField("tactic_id")
    private Long tacticId;

    @TableField("tactic_name")
    private String tacticName;
}
