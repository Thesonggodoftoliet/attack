package com.littlepants.attack.attackweb.model;

import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Source {
    private List<Fact> facts;
    private List<Relationship> relationships;
    private String plugin;
    private List<Adjustment> adjustments;
    private String id;
    private String name;
    private List<Rule> rules;
}
