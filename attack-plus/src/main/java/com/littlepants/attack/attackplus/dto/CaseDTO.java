package com.littlepants.attack.attackplus.dto;

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
    private Long id;
    private Long testcaseId;
    private Long operationId;
    private String name;
    private String tactic;
    private String outcome;
    //ID
    private String technique;
}
