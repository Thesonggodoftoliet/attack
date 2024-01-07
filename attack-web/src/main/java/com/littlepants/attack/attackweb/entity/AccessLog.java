package com.littlepants.attack.attackweb.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName(value = "access_log")
public class AccessLog {
    @TableId
    private String id;

    @TableField(value = "username")
    private String userName;

    @TableField(value = "log_event")
    private String logEvent;

    @TableField(value = "remote_addr")
    private String remoteAddress;//远程IP地址

    @TableField(value = "create_time")
    private Timestamp createTime;
}
