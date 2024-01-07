package com.littlepants.attack.attackplus.service.impl;

import com.littlepants.attack.attackplus.entity.Case;
import com.littlepants.attack.attackplus.entity.Operation;
import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.entity.TestcaseMs;
import com.littlepants.attack.attackplus.mapper.TestcaseMapper;
import com.littlepants.attack.attackplus.service.TestcaseMsService;
import com.littlepants.attack.attackplus.service.TestcaseStrategy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Metasploit测试用例策略
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service("ms")
public class TestcaseMsStrategy implements TestcaseStrategy<TestcaseMs> {
    private final TestcaseMsService testcaseMsService;

    public TestcaseMsStrategy(TestcaseMsService testcaseMsService) {
        this.testcaseMsService = testcaseMsService;
    }

    @Override
    public String getStrategy() {
        return "Metasploit";
    }

    @Override
    public List<TestcaseMs> getTestcasesByIds(List<Long> ids) {
        return testcaseMsService.listByIds(ids);
    }

    @Override
    public List<Testcase> toTestcases(List<TestcaseMs> specificTestcase) {
        List<Testcase> testcaseList = new ArrayList<>();
        for (TestcaseMs ms:specificTestcase){
            Testcase testcase = TestcaseMapper.INSTANCE.msToTestcase(ms);
            testcase.setPlatform("ms");
            testcaseList.add(testcase);
        }
        return testcaseList;
    }

    @Override
    public List<Case> toCases(List<TestcaseMs> specificTestcases, Long operationId) {
        List<Case> testcaseList = new ArrayList<>();
        for (TestcaseMs ms:specificTestcases){
            testcaseList.add(new Case(TestcaseMapper.INSTANCE.msToTestcase(ms),operationId));
        }
        return testcaseList;
    }

    @Override
    public List<Testcase> getAllTestcases() {
        List<TestcaseMs> testcaseMss = testcaseMsService.list();
        return toTestcases(testcaseMss);
    }

    @Override
    public List<Testcase> getTestcasesBy(Map<String, Object> params) {
        List<TestcaseMs> testcaseMss = testcaseMsService.listByMap(params);
        return toTestcases(testcaseMss);
    }

    @Async
    @Override
    public void execute(Operation operation) {
        System.out.println("执行Metasploit");
    }

}
