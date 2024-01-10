package com.littlepants.attack.attackplus.constant;

import lombok.Getter;

/**
 * <p>
 * outcome 枚举
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/10
 */

@Getter
public enum CaseOutcome {
    TBD("TBD"),
    BLOCKED("Blocked"),
    ALERTED("Alerted"),
    LOGGED("logged"),
    NONE("None"),
    N_A("N/A")
    ;

    private final String outcome;

    CaseOutcome(String outcome) {
        this.outcome = outcome;
    }
}
