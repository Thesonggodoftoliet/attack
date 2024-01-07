package com.littlepants.attack.attackplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Operation;
import com.littlepants.attack.attackplus.service.OperationService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/getAll")
    public R<List<Operation>> getAll(){
        return R.success(operationService.list());
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
