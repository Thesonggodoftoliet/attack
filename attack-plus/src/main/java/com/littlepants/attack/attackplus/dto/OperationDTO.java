package com.littlepants.attack.attackplus.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/10
 */

@Data
public class OperationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long campaignId;
    private Integer state;
    private LocalDateTime startTime;
    private String operationName;
    List<Map<String,Object>> progress;
    List<Map<String,Object>> outcome;
}
