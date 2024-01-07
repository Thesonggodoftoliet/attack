package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Operation {
    private Map<String,Object> chain=new HashMap<>();
    private String start="";
    private String group="";
    private Source source=new Source();
    private Adversary adversary=new Adversary();
    private String jitter="";
    private String state="";
    private int autonomous=1;
    private boolean auto_close=true;
    private int visibility=51;
    private List<Agent> host_group=new ArrayList<>();
    private Objective objective=new Objective();
    private String obfuscator="plain-text";
    private String id="";
    private boolean use_learning_parsers=true;
    private String name="";
    private Planner planner;
}
