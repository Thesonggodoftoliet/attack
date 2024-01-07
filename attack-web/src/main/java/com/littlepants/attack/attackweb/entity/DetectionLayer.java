package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@TableName(value = "detection_layer")
public class DetectionLayer {
    @TableId
    private String id;
    private Timestamp createTime;
    @JsonIgnore
    private Timestamp updateTime;
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z\\d_][\\s\\u4E00-\\u9FA5A-Za-z\\d_]{1,119}$",message = "{layer.name.validation}")
    private String layerName;
    private String layerDescription;
}
