package com.littlepants.attack.attackplus.constant;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/10
 */
@Getter
public enum OperationStatus {
    DEFAULT(0),
    PROGRESSING(1),
    FINISHED(2)
    ;
    private final Integer state;

    OperationStatus(Integer state) {
        this.state = state;
    }
}
