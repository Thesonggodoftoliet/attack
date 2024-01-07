package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.TestcaseCaldera;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.service.TestcaseCalderaService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Caldera能力
 前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-06
 */
@RestController
@RequestMapping("/testcaseCaldera")
@Api(tags = "Caldera能力 ")
public class TestcaseCalderaController {
    private final TestcaseCalderaService testcaseCalderaService;
    private final Logger logger = LoggerFactory.getLogger(TestcaseCalderaController.class);

    public TestcaseCalderaController(TestcaseCalderaService testcaseCalderaService) {
        this.testcaseCalderaService = testcaseCalderaService;
    }

    @PostMapping("/addCaldera")
    public R<Boolean> addCaldera(@RequestBody TestcaseCaldera caldera){
        try {
            testcaseCalderaService.save(caldera);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/updateCaldera")
    public R<Boolean> updateCaldera(@RequestBody TestcaseCaldera caldera){
        TestcaseCaldera newCaldera = new TestcaseCaldera();
        newCaldera.setTestcaseName(caldera.getTestcaseName());
        newCaldera.setTestcaseDes(caldera.getTestcaseDes());
        newCaldera.setSupportedPlatforms(caldera.getSupportedPlatforms());
        try {
            testcaseCalderaService.updateById(newCaldera);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @GetMapping("/getAll")
    public R<List<TestcaseCaldera>> getAll(){
        return R.success(testcaseCalderaService.list());
    }

    @GetMapping("/detail/{calderaId}")
    public R<TestcaseCaldera> get(@PathVariable("calderaId")Long calderaId){
        return R.success(testcaseCalderaService.getById(calderaId));
    }
}
