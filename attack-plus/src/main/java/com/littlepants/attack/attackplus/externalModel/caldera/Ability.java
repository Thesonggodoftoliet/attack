package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Ability {
    private boolean singleton;
    private String ability_id;
    private boolean delete_payload;
    private boolean repeatable;
    private String  technique_id;
    private List<String> buckets;
    private String technique_name;
    private Map<String,String> additional_info;
    private String plugin;
    private List<Requirement> requirements;
    private String description;
    private List<Executor> executors;
    private String name;
    private String tactic;
    private String privilege;
    private Access access;
}
