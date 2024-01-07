package com.littlepants.attack.attackweb.model;

import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Goal {
    private String operator;
    private int count;
    private String value;
    private boolean achieved;
    private String target;
}
