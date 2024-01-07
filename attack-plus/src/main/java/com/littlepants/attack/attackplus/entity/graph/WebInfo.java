package com.littlepants.attack.attackplus.entity.graph;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description
 */
@Data
public class WebInfo {
    private String uri;
    private String code;
    private String title;
    private String protocol;
    private Integer port;
    private Integer value;

    public WebInfo(){

    }

    public WebInfo(Map<String,Object> map){
        this.uri = map.getOrDefault("uri","未知").toString().trim();
        this.code = map.getOrDefault("code","未知").toString().trim();
        this.protocol = map.getOrDefault("protocol","未知").toString().trim();
        this.port = Integer.parseInt(map.getOrDefault("port","未知").toString().trim());
        this.title = map.getOrDefault("title","未知").toString().trim();
    }
}
