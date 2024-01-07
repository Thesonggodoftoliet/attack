package com.littlepants.attack.attackplus.externalModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/24
 * @description
 */
@Data
public class Dependencies {
    private String description;
    @JsonProperty("prereq_command")
    private String prereqCommand;
    @JsonProperty("get_prereq_command")
    private String getPrereqCommand;
}
