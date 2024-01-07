package com.littlepants.attack.attackweb.entity;

import com.littlepants.attack.attackweb.validation.BlueTeamValidation;
import lombok.Data;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Data
public class BlueTeam {
    private String outcome;
    @Size(max = 1000,message = "{blue.description.validation}",groups = {BlueTeamValidation.class})
    private String outcomeNote;
    private Timestamp detectionTime;
    private String alertSeverity;
    private String alertTriggered;
    private String activityLogged;
    private List<String> detections;
    private List<String> defends;
    private List<String> detectionLayerIds;
    private List<String> layerNames;
    private List<String> blueTools;
}
