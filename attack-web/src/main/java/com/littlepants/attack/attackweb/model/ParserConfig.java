package com.littlepants.attack.attackweb.model;

import lombok.Data;

import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class ParserConfig {
    private Map<String,String> custom_parser_vals;
    private String edge;
    private String source;
    private String target;
}
