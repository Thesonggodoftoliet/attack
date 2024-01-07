package com.littlepants.attack.attackweb.model;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Adversary {
    private List<String> tags;
    private String adversary_id;
    @Getter
    private boolean has_repeatable_abilities;
    private String Objective;
    private String plugin;
    private String description;
    private String name;
    private List<String> atomic_ordering;
}
