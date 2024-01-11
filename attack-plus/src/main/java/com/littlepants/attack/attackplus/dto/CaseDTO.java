package com.littlepants.attack.attackplus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * Case简略参数
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/27
 */

@Data
public class CaseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long testcaseId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long operationId;
    private String name;
    private Integer state;
    private String tactic;
    private String outcome;
    //ID
    private String technique;
}
