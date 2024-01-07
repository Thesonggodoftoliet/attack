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
public class Requirement {
    private List<Map<String,Object>> relationship_match;
    private String module;
}
