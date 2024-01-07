package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description 与信息容器提供商（CNA 或 ADP）相关的详细信息。
 */
@Data
public class ProviderMetadata {
    private String orgId;
    private String shortName;
    private String dateUpdated;
}
