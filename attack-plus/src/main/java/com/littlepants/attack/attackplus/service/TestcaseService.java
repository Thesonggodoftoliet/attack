package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.Testcase;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/24
 */

public interface TestcaseService extends IService<Testcase> {
    List<Testcase> getAllTestcases();
    List<Testcase> getTestcasesBy(Map<String,Object> params);
    void deleteByIds(List<Long> ids);
}
