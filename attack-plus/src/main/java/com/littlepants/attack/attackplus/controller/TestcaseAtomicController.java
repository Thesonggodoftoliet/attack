package com.littlepants.attack.attackplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.TestcaseAtomic;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.service.TestcaseAtomicService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Atomic测试用例 前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-06
 */
@RestController
@RequestMapping("/testcaseAtomic")
@Api(tags = "Atomic测试用例")
public class TestcaseAtomicController {
    private final Logger logger = LoggerFactory.getLogger(TestcaseAtomicController.class);
    private final TestcaseAtomicService atomicService;

    public TestcaseAtomicController(TestcaseAtomicService atomicService) {
        this.atomicService = atomicService;
    }

    @GetMapping("/getAll")
    public R<List<TestcaseAtomic>> getAll(){
        return R.success(atomicService.list());
    }

    @GetMapping("/getAll/{current}/{count}")
    public R<IPage<TestcaseAtomic>> getAllPage(@PathVariable("current")int current,@PathVariable("count")int count){
        Page<TestcaseAtomic> page = new Page<>(current,count);
        return R.success(atomicService.page(page));
    }

    @GetMapping("/detail/{atomicId}")
    public R<TestcaseAtomic> detail(@PathVariable("atomicId")Long atomicId){
        return R.success(atomicService.getById(atomicId));
    }

    @DeleteMapping("/delete/{atomicId}")
    public R<Boolean> deleteById(@PathVariable("atomicId")Long atomicId){
        try {
            atomicService.removeById(atomicId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail("删除失败");
        }
        return R.success();
    }

    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TestcaseAtomic atomic){
        try {
            atomicService.save(atomic);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TestcaseAtomic atomic){
        TestcaseAtomic newAtomic = new TestcaseAtomic();
        newAtomic.setId(atomic.getId());
        newAtomic.setTestcaseName(atomic.getTestcaseName());
        newAtomic.setTestcaseDes(atomic.getTestcaseDes());
        newAtomic.setSupportedPlatforms(atomic.getSupportedPlatforms());
        try {
            atomicService.updateById(newAtomic);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }
}
