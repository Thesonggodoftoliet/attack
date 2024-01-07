package com.littlepants.attack.attackplus.mapper;

import com.littlepants.attack.attackplus.entity.Testcase;
import com.littlepants.attack.attackplus.entity.TestcaseAtomic;
import com.littlepants.attack.attackplus.entity.TestcaseCaldera;
import com.littlepants.attack.attackplus.entity.TestcaseMs;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * Testcase 映射类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/6
 */

@Mapper
public interface TestcaseMapper {
    TestcaseMapper INSTANCE = Mappers.getMapper(TestcaseMapper.class);

    Testcase calderaToTestcase(TestcaseCaldera testcaseCaldera);

    TestcaseCaldera testcaseToCaldera(Testcase testcase);

    Testcase atomicToTestcase(TestcaseAtomic testcaseAtomic);

    TestcaseAtomic testcaseToAtomic(Testcase testcase);

    Testcase msToTestcase(TestcaseMs testcaseMs);

    TestcaseMs testcaseToMs(Testcase testcase);
}
