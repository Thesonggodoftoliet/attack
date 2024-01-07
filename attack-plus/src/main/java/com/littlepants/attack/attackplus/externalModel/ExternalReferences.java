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
public class ExternalReferences {
    @JsonProperty("source_name")
    private String sourceName;
    private String url;
    @JsonProperty("external_id")
    private String externalId;
}
