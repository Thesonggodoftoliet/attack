package com.littlepants.attack.attackplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.dto.CaseCalderaDTO;
import com.littlepants.attack.attackplus.dto.CaseDTO;
import com.littlepants.attack.attackplus.entity.Case;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.mapper.CaseMapper;
import com.littlepants.attack.attackplus.service.CaseService;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/case")
@Api(tags = "")
public class CaseController {
    private final Logger logger = LoggerFactory.getLogger(CaseController.class);
    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping("/getCase/{page}/{count}")
    public R<Page<Case>> getCases(@PathVariable("page")Long page,@PathVariable("count")Long count){
        Page<Case> casePage = new Page<>(page,count);
        return R.success(caseService.page(casePage));
    }

    @GetMapping("/get/{current}")
    public R<Map<String,Object>> getCasePage(@PathVariable("current")int current,
                                             @RequestParam("operationId")Long operationId){
        logger.info("/get/"+current+" "+operationId);
        QueryWrapper<Case> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_id",operationId);
        Page<Case> casePage = new Page<>(current,10);
        Page<Case> casePages = caseService.page(casePage,queryWrapper);
        Map<String,Object> result = new HashMap<>();
        result.put("records", CaseMapper.INSTANCE.casesToDTOs(casePages.getRecords()));
        result.put("pages",casePages.getPages());
        result.put("size",casePages.getSize());
        result.put("total",casePages.getTotal());
        result.put("current",casePages.getCurrent());
        System.out.println(JsonUtil.toString(result));
        return R.success(result);
    }

    @PostMapping("/updateCase")
    public R<Boolean> updateCase(@RequestBody Case mycase){
        Case sqlcase = new Case();
        sqlcase.setTestcaseName(mycase.getTestcaseName());
        sqlcase.setCommand(mycase.getCommand());
        sqlcase.setCaseDes(mycase.getCaseDes());
        sqlcase.setDefends(mycase.getDefends());
        sqlcase.setAlertSeverity(mycase.getAlertSeverity());
        sqlcase.setActivityLogged(mycase.getActivityLogged());
        sqlcase.setAlertTriggered(mycase.getAlertTriggered());
        sqlcase.setDetectionTime(mycase.getDetectionTime());
        sqlcase.setOutcome(mycase.getOutcome());
        sqlcase.setOutcomeNote(mycase.getOutcomeNote());
        sqlcase.setDetections(mycase.getDetections());
        sqlcase.setId(mycase.getId());
        try {
            caseService.updateById(sqlcase);
        }catch (Exception e){
            logger.error(e.getMessage());
            R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/updateFromCaldera")
    public R<Boolean> updateFromCaldera(@RequestBody String body){
        List<CaseCalderaDTO> list = JsonUtil.toList(body, CaseCalderaDTO.class);
        try {
            caseService.updateFromCaldera(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @GetMapping("/detail/{caseId}")
    public R<Case> getCaseDetail(@PathVariable("caseId")Long caseId){
        return R.success(caseService.getById(caseId));
    }

    @GetMapping("/cases/{operationId}")
    public R<List<CaseDTO>> getCases(@PathVariable("operationId")Long operationId){
        return R.success(caseService.getCasesByOperationId(operationId));
    }

    @PostMapping("/assignTools/{caseId}")
    public R<Boolean> assignTools(@PathVariable("caseId")Long caseId,@RequestBody List<Long> tools){
        try {
            caseService.assignTools(caseId,tools);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }
}
