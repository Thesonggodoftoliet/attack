package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Executor {
    private String build_target;
    private List<String> cleanup;
    private String code;
    private String payloads;
    private String command;
    private String language;
    private List<String> uploads;
    private List<Variation> variations;
    private Map<String,String> additional_info;
    private String name;
    private String platform;
    private List<Parser> parsers;
    private int timeout;
}
