package com.littlepants.attack.attackplus.mapper;

import com.littlepants.attack.attackplus.dto.OperationDTO;
import com.littlepants.attack.attackplus.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/10
 */
@Mapper

public interface OperationMapper {
    OperationMapper INSTANCE = Mappers.getMapper(OperationMapper.class);

    List<OperationDTO> operationToDTOs(List<Operation> operations);
}
