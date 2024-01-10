package com.littlepants.attack.attackplus.constant;

import lombok.Getter;

/**
 * <p>
 * 测试用例的状态描述
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/10
 */

@Getter
public enum CaseStatus {
    TBD(-5),
    InProgress(1),
    Paused(2),
    Completed(0),
    Abandon(-1)
    ;
    private final Integer state;

    CaseStatus(Integer state) {
        this.state = state;
    }
}
