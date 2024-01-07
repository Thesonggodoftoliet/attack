package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/12/2
 * @description
 */
@Data
public class TaxonomyMapping {
    private String taxonomyName;
    private String taxonomyVersion;
    private List<TaxonomyRelation> taxonomyRelations;
}
