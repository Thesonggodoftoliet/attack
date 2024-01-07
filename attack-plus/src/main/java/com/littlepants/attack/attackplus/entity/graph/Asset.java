package com.littlepants.attack.attackplus.entity.graph;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/17
 * @description 资产
 */
@Data
@Node("Asset")
public class Asset implements Serializable {
    @Id
    @GeneratedValue
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String ip;
    private String name;
    private String os;
    private String paw;
    private Integer hostId;//nessus 扫描hostid
    @Property("os_version")
    private String osVersion;//操作系统版本
    @Property("asset_type")
    private String assetType;//资产类型：交换机、安全设备、主机、服务器等
    @Property("web_infos")
    private String webInfos;
    private List<WebInfo> webInfoList;
    @Property("firewall_policies")
    private String firewallPolicies;//防火墙策略
    private List<FirewallPolicy> firewallPolicyList;
    @Relationship(value = "DEPLOY",direction = Relationship.Direction.OUTGOING)
    private List<Deploy> services;
    @Relationship(value = "EXIST",direction = Relationship.Direction.OUTGOING)
    private Set<Vulnerability> vulnerabilities = new HashSet<>();
    @Relationship(value = "COMMUNICATE",direction = Relationship.Direction.OUTGOING)
    private List<Asset> assets;
    @Relationship(value = "BELONG",direction = Relationship.Direction.OUTGOING)
    private Domain domain;
    public Asset(){

    }

    public Asset(String ip){
        this.ip = ip;
    }
}
