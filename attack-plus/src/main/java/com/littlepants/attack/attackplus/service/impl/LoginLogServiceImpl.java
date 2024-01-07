package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.entity.LoginLog;
import com.littlepants.attack.attackplus.service.LoginLogService;
import com.littlepants.attack.attackplus.dao.LoginLogDao;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-23
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogDao, LoginLog> implements LoginLogService {

}
