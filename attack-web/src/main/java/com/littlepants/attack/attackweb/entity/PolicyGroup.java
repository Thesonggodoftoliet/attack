package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Data
@TableName(value = "policy_group")
@Getter
@Setter
public class PolicyGroup implements GrantedAuthority {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "name")
    private String roleName;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
