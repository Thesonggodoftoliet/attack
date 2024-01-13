package com.littlepants.attack.attackplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Tech;
import com.littlepants.attack.attackplus.service.TechService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/tech")
@Api(tags = "")
public class TechController {
    private final TechService techService;

    public TechController(TechService techService) {
        this.techService = techService;
    }

    @GetMapping("/get")
    public R<List<Tech>> get(){
        return R.success(techService.list());
    }

    @GetMapping("/get-by-tactic")
    public R<List<Tech>> getByTactic(@RequestParam("tactic")String tactic){
        QueryWrapper<Tech> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tactic_name",tactic);
        return R.success(techService.list(queryWrapper));
    }

}
