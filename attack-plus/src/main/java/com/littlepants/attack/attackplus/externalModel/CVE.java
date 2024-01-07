package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description
 */
@Data
public class CVE {
    private String dataType;
    private String dataVersion;
    private CveMetadata cveMetadata;
    private Containers containers;
}
