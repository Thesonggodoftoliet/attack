package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Link {
    private int cleanup;
    private int score;
    private String paw;
    private int jitter;
    private int status;
    private String pid;
    private String finish;
    private String command;
    private Ability ability;
    private Visibility visibility;
    private String id;
    private Executor executor;
    private String host;
    private List<Fact> facts;
    private String unique;
    private boolean deadman;
    private String decide;
    private List<Fact> used;
    private int pin;
    private List<Relationship> relationships;
    private String agent_reported_time;
    private String collect;
    private String output;
}
