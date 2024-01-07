package com.littlepants.attack.attackplus.entity.graph;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;

import java.io.Serializable;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description 资产提供的服务
 */
@Data
public class Port implements Serializable {
    private String port;// 22/tcp
    private String state;// closed
    private String service;// ssh
    private String version;

    @Override
    public String toString() {
        return "Port{" +
                "port:" + port +
                ", state:'" + state + '\'' +
                ", service:'" + service + '\'' +
                ", version:'" + version + '\'' +
                '}';
    }
}
