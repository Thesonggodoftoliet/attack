package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.littlepants.attack.attackweb.validation.TeamInfoValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@TableName(value = "team")
public class Team {
    @TableId
    private String id;

    @TableField(value = "create_time")
    @JsonIgnore
    private Timestamp createTime;

    @JsonIgnore
    @TableField(value = "update_time")
    private Timestamp updateTime;

    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9]{2,20}$",message = "{team.name.validation}",
            groups = {TeamInfoValidation.class})
    @TableField(value = "team_name")
    private String teamName;

    @Pattern(regexp = "^$|^[A-Za-z]{4,10}$",message = "{team.abbr.validation}",groups = {TeamInfoValidation.class})
    private String abbreviation;

    @TableField(value = "team_description")
    @Size(max = 200,message = "{team.description.size}",groups = {TeamInfoValidation.class})
    private String teamDescription;

    @Pattern(regexp = "^$|^[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?$",
            message = "{team.url.validation}",groups = {TeamInfoValidation.class})
    private String url;//组织的网站
}
