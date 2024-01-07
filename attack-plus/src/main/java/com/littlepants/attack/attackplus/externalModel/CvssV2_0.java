package com.littlepants.attack.attackplus.externalModel;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/12/2
 * @description
 */
public class CvssV2_0 {
    private String version;
    private String vectorString;
    private String accessVector;
    private String accessComplexity;
    private String authentication;
    private String confidentialityImpact;
    private String integrityImpact;
    private String availabilityImpact;
    private double baseScore;
    private String exploitability;
    private String remediationLevel;
    private String reportConfidence;
    private double temporalScore;
    private String collateralDamagePotential;
    private String targetDistribution;
    private String confidentialityRequirement;
    private String integrityRequirement;
    private String availabilityRequirement;
    private double environmentalScore;
}
