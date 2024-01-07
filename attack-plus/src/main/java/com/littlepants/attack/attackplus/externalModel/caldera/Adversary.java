package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/12
 */

@Data
public class Adversary {
    private List<String> tags=new ArrayList<>();
    private String adversary_id="";
    @Getter
    private boolean has_repeatable_abilities=false;
    private String Objective="";
    private String plugin="";
    private String description="";
    private String name="";
    private List<String> atomic_ordering=new ArrayList<>();
}
