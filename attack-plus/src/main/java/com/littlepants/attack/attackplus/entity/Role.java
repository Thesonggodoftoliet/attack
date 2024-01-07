package com.littlepants.attack.attackplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-23
 */
@Data
@TableName("att_role")
@ApiModel(value = "Role", description = "角色表")
public class Role implements Serializable, GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("角色名称")
    @TableField("name")
    private String name;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }
}
