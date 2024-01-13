package com.littlepants.attack.attackplus.service.impl;

import com.littlepants.attack.attackplus.entity.Case;
import com.littlepants.attack.attackplus.entity.Operation;
import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.entity.TestcaseAtomic;
import com.littlepants.attack.attackplus.mapper.TestcaseMapper;
import com.littlepants.attack.attackplus.service.TestcaseAtomicService;
import com.littlepants.attack.attackplus.service.TestcaseStrategy;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Atomic测试用例策略
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@Service("atomic")
public class TestcaseAtomicStrategy implements TestcaseStrategy<TestcaseAtomic> {
    private final TestcaseAtomicService testcaseAtomicService;

    public TestcaseAtomicStrategy(TestcaseAtomicService testcaseAtomicService) {
        this.testcaseAtomicService = testcaseAtomicService;
    }

    @Override
    public String getStrategy() {
        return "Atomic";
    }

    @Override
    public List<TestcaseAtomic> getTestcasesByIds(List<Long> ids) {
        return testcaseAtomicService.listByIds(ids);
    }

    @Override
    public List<Testcase> toTestcases(List<TestcaseAtomic> specificTestcases) {
        List<Testcase> testcaseList = new ArrayList<>();
        for (TestcaseAtomic atomic:specificTestcases){
            Testcase testcase = TestcaseMapper.INSTANCE.atomicToTestcase(atomic);
            testcase.setPlatform("atomic");
            testcaseList.add(testcase);
        }
        return testcaseList;
    }

    @Override
    public List<Case> toCases(List<TestcaseAtomic> specificTestcases, Long operationId) {
        List<Case> testcaseList = new ArrayList<>();
        for (TestcaseAtomic atomic:specificTestcases){
            testcaseList.add(new Case(TestcaseMapper.INSTANCE.atomicToTestcase(atomic),operationId));
        }
        return testcaseList;
    }

    @Override
    public Case toCase(TestcaseAtomic specificTestcase, Long operationId) {
        return new Case(TestcaseMapper.INSTANCE.atomicToTestcase(specificTestcase), operationId);
    }

    @Override
    public Case toCase(Long id, Long operationId) {
        TestcaseAtomic atomic = testcaseAtomicService.getById(id);
        return toCase(atomic,operationId);
    }

    @Override
    public List<Testcase> getAllTestcases() {
        List<TestcaseAtomic> atomics = testcaseAtomicService.list();
        return toTestcases(atomics);
    }

    @Override
    public List<Testcase> getTestcasesBy(Map<String, Object> params) {
        List<TestcaseAtomic> atomics = testcaseAtomicService.listByMap(params);
        return toTestcases(atomics);
    }

    @Async
    @Override
    public void execute(Operation operation) {
        System.out.println("执行Atomic");
    }


}
