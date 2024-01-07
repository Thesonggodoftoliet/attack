package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Campaign;
import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.service.CampaignService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 测试用例的顺序等 前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/campaign")
@Api(tags = "测试用例的顺序等")
@Slf4j
public class CampaignController {
    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/getAll")
    public R<List<Campaign>> getAllCampaigns(){
        return R.success(campaignService.list());
    }

    @PostMapping("/updateCampaign")
    public R<Boolean> updateCampaign(@RequestBody Campaign campaign){
        try {
            campaignService.updateCampaign(campaign);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @DeleteMapping("/delete/{campaignId}")
    public R<Boolean> deleteCampaign(@PathVariable Long campaignId){
        try {
            campaignService.deleteCampaignById(campaignId);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/assignTestcase")
    public R<Boolean> assignTestcase(@RequestBody Campaign campaign, @RequestBody List<Testcase> testcases){
        try {
            campaignService.assignTestcases(campaign, testcases);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

}
