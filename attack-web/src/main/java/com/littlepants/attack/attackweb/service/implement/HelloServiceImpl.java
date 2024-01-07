package com.littlepants.attack.attackweb.service.implement;

import com.littlepants.attack.attackweb.service.intf.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/12/11
 */

@DubboService
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String message) {
        return "Hello, "+message;
    }
}
