package com.littlepants.attack.attackplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.service.TestcaseService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/5
 */
@RestController
@RequestMapping("/testcase")
@Api(tags = "测试用例 ")
public class TestcaseController {
    private final TestcaseService testcaseService;

    public TestcaseController(TestcaseService testcaseService) {
        this.testcaseService = testcaseService;
    }

    @GetMapping("/getAll")
    public R<List<Testcase>> getAllTestCases(){
        return R.success(testcaseService.list());
    }

    @GetMapping("/getTestcases")
    public R<List<Testcase>> getTestcases(@RequestBody Map<String,Object> params){
        return R.success(testcaseService.getTestcasesBy(params));
    }

    @GetMapping("/getAll/{current}/{count}")
    public R<IPage<Testcase>> getTestcasePage(@PathVariable("current")int current,@PathVariable("count")int count){
        Page<Testcase> page = new Page<>(current,count);
        return R.success(testcaseService.page(page));
    }

}
