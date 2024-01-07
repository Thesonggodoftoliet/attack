package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;
import lombok.Getter;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Relationship {
    @Getter
    private String unique;
    private int score;
    private Fact source;
    private String edge;
    private Target target;
    private String origin;
}
