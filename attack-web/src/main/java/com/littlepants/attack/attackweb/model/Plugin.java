package com.littlepants.attack.attackweb.model;

import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/14
 */
@Data
public class Plugin {
    private String name;
    private String description;
    private String address;
    private boolean enabled;
}
