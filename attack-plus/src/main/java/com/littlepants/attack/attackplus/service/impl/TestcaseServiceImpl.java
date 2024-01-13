package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.TestcaseDao;
import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.service.TestcaseService;
import com.littlepants.attack.attackplus.service.TestcaseStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

@Service
public class TestcaseServiceImpl extends ServiceImpl<TestcaseDao, Testcase> implements TestcaseService {
    private final ApplicationContext applicationContext;
    private final List<String> platforms;

    public TestcaseServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        platforms = new ArrayList<>();
        platforms.add("caldera");
        platforms.add("ms");
        platforms.add("atomic");
    }

    @Override
    public List<Testcase> getAllTestcases() {
        List<Testcase> testcases = new ArrayList<>();
        for (String platform:platforms){
            TestcaseStrategy strategy = applicationContext.getBean(platform,TestcaseStrategy.class);
            testcases.addAll(strategy.getAllTestcases());
        }
        return testcases;
    }

    @Override
    public List<Testcase> getTestcasesBy(Map<String, Object> params) {
        List<Testcase> testcases = new ArrayList<>();
        for (String platform:platforms){
            TestcaseStrategy strategy = applicationContext.getBean(platform,TestcaseStrategy.class);
            testcases.addAll(strategy.getTestcasesBy(params));
        }
        return testcases;
    }


    @Override
    public void deleteByIds(List<Long> ids) {
        //暂时不需要删除
    }
}
