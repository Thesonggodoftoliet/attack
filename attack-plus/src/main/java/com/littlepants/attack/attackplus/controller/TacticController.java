package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Tactic;
import com.littlepants.attack.attackplus.service.TacticService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2024-01-13
 */
@RestController
@RequestMapping("/tactic")
@Api(tags = "")
public class TacticController {
    private final TacticService tacticService;

    public TacticController(TacticService tacticService) {
        this.tacticService = tacticService;
    }

    @GetMapping("/get")
    public R<List<Tactic>> get(){
        return R.success(tacticService.list());
    }
}
