package com.littlepants.attack.attackplus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用于和Caldera传递参数
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/19
 */

@Data
public class CaseCalderaDTO implements Serializable {
    @Serial
    private static final long serialVersionUID =1L;
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("end_time")
    private LocalDateTime endTime;
    private String command;
    @JsonProperty("technique_name")
    private String techniqueName;
    @JsonProperty("case_des")
    private String caseDes;
    @JsonProperty("technique_id")
    private String techniqueId;
    private String tactic;
    private int status;
    @JsonProperty("operation_id")
    private Long operationId;
    @JsonProperty("ability_id")
    private String abilityId;
}
