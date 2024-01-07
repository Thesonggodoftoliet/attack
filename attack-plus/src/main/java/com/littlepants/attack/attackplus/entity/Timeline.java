package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.littlepants.attack.attackplus.constant.AttackLog;
import io.swagger.annotations.ApiModel;
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
 * @since 2023-07-05
 */
@Data
@TableName("att_timeline")
@ApiModel(value = "Timeline", description = "")
public class Timeline implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField("case_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long caseId;

    @TableField("timeline")
    private LocalDateTime timeline;

    @TableField("field_name")
    private String fieldName;

    @TableField("field_action")
    private String fieldAction;

    @TableField("ip")
    private String ip;

    public Timeline(Long caseId, String fieldName, String fieldAction, String ip) {
        this.caseId = caseId;
        this.fieldName = fieldName;
        this.fieldAction = fieldAction;
        this.ip = ip;
    }

    public Timeline(AttackLog attackLog, Long caseId){
        this.fieldAction = attackLog.getMsg();
        this.caseId = caseId;
    }
}
