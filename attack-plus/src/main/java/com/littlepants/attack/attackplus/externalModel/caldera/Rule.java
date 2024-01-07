package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Rule {
    private String trait;
    private String match;
    private Map<String,Object> action;
}
