package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Source {
    private List<Fact> facts=new ArrayList<>();
    private List<Relationship> relationships=new ArrayList<>();
    private String plugin="stockpile";
    private List<Adjustment> adjustments=new ArrayList<>();
    private String id="ed32b9c3-9593-4c33-b0db-e2007315096b";
    private String name="basic";
    private List<Rule> rules=new ArrayList<>();
}
