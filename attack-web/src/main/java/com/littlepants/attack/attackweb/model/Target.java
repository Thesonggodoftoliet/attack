package com.littlepants.attack.attackweb.model;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Target {
    @Getter
    private String unique;
    private int score;
    private String source;
    private String relationships;
    private int limit_count;
    private String technique_id;
    private Map<String,Object> origin_type;
    @Getter
    private String created;
    private Map<String,Object> value;
    private List<String> links;
    private List<String> collected_by;
    @Getter
    private String name;
    private String trait;
}
