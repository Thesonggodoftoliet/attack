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
public class Operation {
    private Map<String,Object> chain;
    private String start;
    private String group;
    private Source source;
    private Adversary adversary;
    private String jitter;
    private String state;
    private int autonomous;
    private boolean auto_close;
    private int visibility;
    private List<Agent> host_group;
    private Objective objective;
    private String obfuscator;
    private String id;
    private boolean use_learning_parsers;
    private String name;
    private Planner planner;
}
