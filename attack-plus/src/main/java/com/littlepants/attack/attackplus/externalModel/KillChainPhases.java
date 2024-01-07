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
public class KillChainPhases {
    @JsonProperty("kill_chain_name")
    private String killChainName;
    @JsonProperty("phase_name")
    private String phaseName;
}
