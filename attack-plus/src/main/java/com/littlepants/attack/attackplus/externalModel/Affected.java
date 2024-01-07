package com.littlepants.attack.attackplus.externalModel;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/12/2
 * @description
 */
public class Affected {
    private String vendor;
    private String product;
    private List<String> platforms;
    private String collectionUrl;
    private String packageName;
    private List<String> cpes;
    private String repo;
    private List<String> modules;
    private List<String> programFiles;
    private List<ProgramRoutine> programRoutines;
    private List<Map<String,String>> versions;
    private String defaultStatus;
}
