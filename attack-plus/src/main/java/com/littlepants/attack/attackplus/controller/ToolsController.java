package com.littlepants.attack.attackplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Tools;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.service.ToolsService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/tools")
@Api(tags = "")
public class ToolsController {
    private final Logger logger = LoggerFactory.getLogger(ToolsController.class);
    private final ToolsService toolsService;

    public ToolsController(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    @GetMapping("/getAll")
    public R<List<Tools>> getAll(){
        return R.success(toolsService.list());
    }

    @GetMapping("/getAll/{current}/{count}")
    public R<IPage<Tools>> getPage(@PathVariable("current")int current, @PathVariable("count")int count){
        Page<Tools> page = new Page<>(current,count);
        return R.success(toolsService.page(page));
    }

    @GetMapping("/getVendors")
    public R<List<Map<String,Object>>> getVendors(){
        return R.success(toolsService.getVendors());
    }

    @GetMapping("/tools")
    public R<List<Tools>> getToolsByVendorName(@RequestParam("vendor_name")String vendorName){
        return R.success(toolsService.getToolsByVendor(vendorName));
    }

    @PostMapping("/add")
    public R<Boolean> add(@RequestBody Tools tools){
        try {
            toolsService.save(tools);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/update")
    public R<Boolean> update(@RequestBody Tools tools){
        Tools newTool = new Tools();
        newTool.setId(tools.getId());
        newTool.setToolVersion(tools.getToolVersion());
        newTool.setToolVendor(tools.getToolVendor());
        newTool.setToolDescription(tools.getToolDescription());
        try {
            toolsService.updateById(newTool);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @GetMapping("/delete")
    public R<Boolean> deleteById(@RequestParam("toolId")Long toolId){
        try {
            toolsService.removeById(toolId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

}
