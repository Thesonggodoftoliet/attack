package com.littlepants.attack.attackplus.mapper;

import com.littlepants.attack.attackplus.dto.CaseCalderaDTO;
import com.littlepants.attack.attackplus.dto.CaseDTO;
import com.littlepants.attack.attackplus.entity.Case;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/19
 */

@Mapper
public interface CaseMapper {
    CaseMapper INSTANCE = Mappers.getMapper(CaseMapper.class);

    Case paramDTOToCase(CaseCalderaDTO caseCalderaDTO);

    List<Case> paramDTOsToCases(List<CaseCalderaDTO> caseCalderaDTOS);

    @Mapping(target = "name",source = "testcaseName")
    @Mapping(target = "technique",source = "techniqueName")
    @Mapping(target = "state",source = "state")
    CaseDTO caseToDTO(Case mycase);

    @Mapping(target = "name",source = "testcaseName")
    @Mapping(target = "technique",source = "techniqueName")
    @Mapping(target = "state",source = "state")
    List<CaseDTO> casesToDTOs(List<Case> cases);
}
