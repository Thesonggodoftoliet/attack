package com.littlepants.attack.attackweb.model;

import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Parser {
    private List<ParserConfig> parserconfigs;
    private String module;
}
