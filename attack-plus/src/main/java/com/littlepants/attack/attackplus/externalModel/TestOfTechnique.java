package com.littlepants.attack.attackplus.externalModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/24
 * @description
 */
@Data
public class TestOfTechnique {
    @JsonProperty("attack_technique")
    private String attackTechnique;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("atomic_tests")
    private List<AtomicTests> atomicTests;
}
