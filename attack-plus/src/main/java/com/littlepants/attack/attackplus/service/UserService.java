package com.littlepants.attack.attackplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.littlepants.attack.attackplus.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-21
 */
public interface UserService extends IService<User>{
    boolean assignRole(User user,String roleName);
    User getUserByAccount(String account);
    User getUserById(Long userId);
}
