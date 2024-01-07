package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Timeline;
import com.littlepants.attack.attackplus.service.TimelineService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/timeline")
@Api(tags = "")
public class TimelineController {
    private final Logger logger = LoggerFactory.getLogger(TimelineController.class);
    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @GetMapping("/{caseId}")
    public R<List<Timeline>> getTimelines(@PathVariable("caseId")Long caseId){
        return R.success(timelineService.getTimelines(caseId));
    }
}
