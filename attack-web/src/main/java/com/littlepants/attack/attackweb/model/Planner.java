package com.littlepants.attack.attackweb.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Planner {
    private boolean allow_repeatable_abilities;
    private String module;
    private List<String> ignore_enforcement_modules;
    private String plugin;
    private Map<String,Object> params;
    private String description;
    private String id;
    private List<Fact> stopping_conditions;
    private String name;
}
