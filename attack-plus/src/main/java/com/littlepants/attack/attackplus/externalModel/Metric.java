package com.littlepants.attack.attackplus.externalModel;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/12/2
 * @description
 */
public class Metric {
    private String format;
    private List<Scenario> scenarios;
    private CvssV3_1 cvssV3_1;
    private CvssV3_0 cvssV3_0;
    private CvssV2_0 cvssV2_0;
    private Map<String,String> other;
}
