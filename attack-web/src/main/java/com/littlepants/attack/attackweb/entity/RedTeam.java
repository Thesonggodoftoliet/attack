package com.littlepants.attack.attackweb.entity;

import com.littlepants.attack.attackweb.validation.RedTeamValidation;
import lombok.Data;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
public class RedTeam {
    private Timestamp startTime;
    private Timestamp endTime;
    private String technique;
    private String guid;
    private String mitredId;
    private List<String> references;
    private String status;
    private List<String> targetAssets;
    private List<String> sourceIPs;
    private List<String> redTools;
    private String phase;
    @Size(max = 1000,message = "{red.description.validation}",groups = {RedTeamValidation.class})
    private String description;
    private List<Map<String,Object>> redToolInfo;
}
