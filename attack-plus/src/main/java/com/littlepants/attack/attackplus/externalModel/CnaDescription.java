package com.littlepants.attack.attackplus.externalModel;

import lombok.Data;

import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/11/18
 * @description
 */
@Data
public class CnaDescription {
    private String lang;
    private String value;
    private List<SupportingMedia> supportingMedia;
}
