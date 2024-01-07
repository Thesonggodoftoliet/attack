package com.littlepants.attack.attackweb.model;

import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/14
 */
@Data
public class CalderaInfo {
    private String application;
    private String version;
    private List<Plugin> plugins;
}
