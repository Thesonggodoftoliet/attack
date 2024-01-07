package com.littlepants.attack.attackplus.entity.graph;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/21
 * @description
 */
@Data
public class FirewallPolicy {
    private String srcIp;
    private String desIp;
    private Integer srcPort;
    private Integer desPort;
    private Boolean enable;
    private String policy;// ALLOW DENY
    private String protocol;
}
