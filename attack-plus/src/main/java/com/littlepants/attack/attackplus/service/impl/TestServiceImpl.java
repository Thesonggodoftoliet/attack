package com.littlepants.attack.attackplus.service.impl;

import com.littlepants.attack.attackplus.dao.UserDao;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.service.TestService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/26
 */

@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Setter
    private UserDao userDao;
    @Override
    @Transactional(value = "transactionManager")
    public void testMultiTransaction() {
        try {
            User user = new User();
            user.setUserAcct("test");
            user.setPhone("11111111111");
            user.setEmail("test@gmail.com");
            user.setNickName("test");
            user.setUserPwd("$2a$10$2g12SR/43US1QRKg3WiYhuXf/Ao6NyrBeSXpsmvL9WrFfxZLNRB7W");
            userDao.insert(user);
            userDao.insert(user);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Async
    @Override
    public void testAsync() {
        throw new RuntimeException("测试");
//        try {
//            for (int i=1;i<=100;i++) {
//                logger.info(Thread.currentThread().getName()+"-----------异步:>"+i);
//                Thread.sleep(500);
//            }
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//        System.out.println("数据正在处理...");
    }
}
