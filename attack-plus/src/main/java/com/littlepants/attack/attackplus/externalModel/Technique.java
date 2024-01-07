package com.littlepants.attack.attackplus.externalModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/24
 * @description 来自ATT&CK的technique
 */
@Data
public class Technique {
    private String type;
    private String id;
    @JsonProperty("created_by_ref")
    private String createdByRef;
    private Date created;
    private Date modified;
    private String name;
    private String description;
    @JsonProperty("kill_chain_phases")
    private List<KillChainPhases> killChainPhases;
    @JsonProperty("external_references")
    private List<ExternalReferences> externalReferences;
    @JsonProperty("object_marking_refs")
    private List<String> objectMarkingRefs;
    @JsonProperty("x_mitre_attack_spec_version")
    private Date xMitreAttackSpecVersion;
    @JsonProperty("x_mitre_contributors")
    private List<String> xMitreContributors;
    @JsonProperty("x_mitre_data_sources")
    private List<String> xMitreDataSources;
    @JsonProperty("x_mitre_deprecated")
    private boolean xMitreDeprecated;
    @JsonProperty("x_mitre_detection")
    private String xMitreDetection;
    @JsonProperty("x_mitre_domains")
    private List<String> xMitreDomains;
    @JsonProperty("x_mitre_is_subtechnique")
    private boolean xMitreIsSubtechnique;
    @JsonProperty("x_mitre_modified_by_ref")
    private String xMitreModifiedByRef;
    @JsonProperty("x_mitre_platforms")
    private List<String> xMitrePlatforms;
    @JsonProperty("x_mitre_version")
    private String xMitreVersion;
}
