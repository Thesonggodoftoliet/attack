package com.littlepants.attack.attackplus.service;

import com.littlepants.attack.attackplus.entity.Case;
import com.littlepants.attack.attackplus.entity.Operation;
import com.littlepants.attack.attackplus.entity.Testcase;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/5
 */

public interface TestcaseStrategy<T> {
    String getStrategy();
    List<T> getTestcasesByIds(List<Long> ids);
    List<Testcase> toTestcases(List<T> specificTestcase);
    List<Case> toCases(List<T> specificTestcases, Long operationId);
    List<Testcase> getAllTestcases();
    List<Testcase> getTestcasesBy(Map<String,Object> params);
    void execute(Operation operation) throws TimeoutException;
}
