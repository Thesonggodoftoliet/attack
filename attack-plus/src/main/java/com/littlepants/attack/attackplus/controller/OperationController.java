package com.littlepants.attack.attackplus.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.dto.OperationDTO;
import com.littlepants.attack.attackplus.entity.Operation;
import com.littlepants.attack.attackplus.mapper.OperationMapper;
import com.littlepants.attack.attackplus.service.CaseService;
import com.littlepants.attack.attackplus.service.OperationService;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 对应Caldera的 operations (campaign)
 前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/operation")
@Api(tags = "对应Caldera的 operations (campaign) ")
@Slf4j
public class OperationController {
    private final OperationService operationService;
    private final CaseService caseService;

    public OperationController(OperationService operationService, CaseService caseService) {
        this.operationService = operationService;
        this.caseService = caseService;
    }

    @GetMapping("/getAll")
    public R<List<Operation>> getAll(){
        return R.success(operationService.list());
    }

    @GetMapping("/get/{current}")
    public R<Map<String,Object>> getOperationPage(@PathVariable("current")int current){
        Page<Operation> operationPage = new Page<>(current,10);
        Page<Operation> operationPages = operationService.page(operationPage);
        List<OperationDTO> operationDTOList = OperationMapper.INSTANCE.operationToDTOs(operationPages.getRecords());
        for (OperationDTO dto:operationDTOList){
            Map<String,List<Map<String,Object>>> result = caseService.getBarByOperationId(dto.getId());
            dto.setOutcome(result.get("outcome"));
            dto.setProgress(result.get("progress"));
        }
        Map<String,Object> result = new HashMap<>();
        result.put("records",operationDTOList);
        result.put("pages",operationPages.getPages());
        result.put("size",operationPages.getSize());
        result.put("total",operationPages.getTotal());
        result.put("current",operationPages.getCurrent());
        return R.success(result);
    }
    @GetMapping("/getAll/{page}/{count}")
    public R<IPage<Operation>> getAllPage(@PathVariable("page")int page, @PathVariable("count")int count){
        Page<Operation> operationPage = new Page<>(page,count);
        return R.success(operationService.page(operationPage));
    }

    @PostMapping("/updateOperation")
    public R<Boolean> updateOperation(@RequestBody Operation operation){
        Operation newOperation = new Operation();
        newOperation.setId(operation.getId());
        newOperation.setOperationName(operation.getOperationName());
        try {
            operationService.updateById(newOperation);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail("更新失败");
        }
        return R.success();
    }

    @DeleteMapping("/delete/{id}")
    public R<Boolean> deleteOperation(@PathVariable("id")Long id){
        try {
            operationService.deleteOperation(id);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail("删除失败");
        }
        return R.success();
    }

    @PostMapping("/addOperation")
    public R<Boolean> addOperation(@RequestBody Operation operation){
        try {
            operationService.addOperationFromCampaign(operation);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail("新增失败");
        }
        return R.success();
    }

    @GetMapping("/execute/{id}")
    public R<Boolean> execute(@PathVariable("id")Long id){
        try {
            operationService.execute(id);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail("执行失败");
        }
        return R.success();
    }

    @PostMapping("/assignIps/{id}")
    public R<Boolean> assignIpsByOperation(@PathVariable("id")Long operationId,@RequestBody List<Long> ips){
        Operation operation = new Operation();
        operation.setId(operationId);
        try {
            operationService.assignIps(operation,ips);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.fail("分配IP失败");
        }
        return R.success();
    }

}
