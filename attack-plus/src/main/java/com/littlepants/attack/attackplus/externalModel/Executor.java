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
public class Executor {
    private String command;
    @JsonProperty("cleanup_command")
    private String cleanupCommand;
    private String name;
    @JsonProperty("elevation_required")
    private boolean elevationRequired;
}
