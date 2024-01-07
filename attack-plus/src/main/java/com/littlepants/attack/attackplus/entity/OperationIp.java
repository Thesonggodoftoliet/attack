package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 将Operation和IP连接
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-07
 */
@Data
@TableName("att_operation_ip")
@ApiModel(value = "OperationIp", description = "")
public class OperationIp implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @TableField("ip_id")
    private Long ipId;

    @TableField("operation_id")
    private Long operationId;

    public OperationIp(Long ipId, Long operationId) {
        this.ipId = ipId;
        this.operationId = operationId;
    }
}
