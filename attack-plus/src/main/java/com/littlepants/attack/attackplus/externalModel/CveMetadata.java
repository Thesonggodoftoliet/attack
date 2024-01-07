package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description
 */
@Data
public class CveMetadata {
    private String cveId;
    private String assignerOrgId;
    private String assignerShortName;
    private String requesterUserId;
    private String dateUpdated;
    private Integer serial;
    private String dateReserved;
    private String datePublished;
    private String state;
}
