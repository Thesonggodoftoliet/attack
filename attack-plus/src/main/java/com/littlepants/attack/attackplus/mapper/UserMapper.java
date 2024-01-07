package com.littlepants.attack.attackplus.mapper;

import com.littlepants.attack.attackplus.dto.UserDTO;
import com.littlepants.attack.attackplus.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 *  User 映射类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/24
 */
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userAccount", source = "userAcct")
    @Mapping(target = "status", source = "enableState")
    @Mapping(target = "lastLoginTime", source = "updateTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    UserDTO userToUserDto(User user);



}
