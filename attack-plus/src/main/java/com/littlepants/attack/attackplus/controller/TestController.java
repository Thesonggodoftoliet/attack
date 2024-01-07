package com.littlepants.attack.attackplus.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.entity.XUser;
import com.littlepants.attack.attackplus.service.TestService;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final String key = "myKey";
    private final String region="rx";
    @Autowired
    private CacheChannel cacheChannel;
    @Autowired
    private TestService testService;
    @NacosInjected
    private NamingService namingService;

    @GetMapping("/testAsync")
    public String testAsync(){
        logger.info(Thread.currentThread().getName()+"---------执行异步---------");
        try {
            testService.testAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Async";
    }

    @GetMapping("/testMulti")
    public String testMulti(){
        testService.testMultiTransaction();
        return "test";
    }

    @GetMapping("/getInfos")
    public List<String> getInfos(){
        CacheObject cacheObject = cacheChannel.get(region, key);
        if(cacheObject.getValue() == null){
            //缓存中没有找到，查询数据库获得
            List<String> data = new ArrayList<String>();
            data.add("info1");
            data.add("info2");
            //放入缓存
            cacheChannel.set(region,key,data);
            return data;
        }
        return (List<String>) cacheObject.getValue();
    }

    //清理指定缓存
    @GetMapping("/evict")
    public String evict(){
        cacheChannel.evict(region,key);
        return "evict success";
    }

    //检测存在哪级缓存
    @GetMapping("/check")
    public String check(){
        int check = cacheChannel.check(region, key);
        return "level:" + check;
    }

    //检测缓存数据是否存在
    @GetMapping("/exists")
    public String exists(){
        boolean exists = cacheChannel.exists(region, key);
        return "exists:" + exists;
    }

    //清理指定区域的缓存
    @GetMapping("/clear")
    public String clear(){
        cacheChannel.clear(region);
        return "clear success";
    }

    @GetMapping("/getUser")
    public User getUser(){
        XUser xUser = new XUser();
        xUser.setUserAcct("123456");
        return xUser;
    }

    @GetMapping("/get")
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    @GetMapping("/getAll")
    @ResponseBody
    public ListView<String> getAll() throws NacosException {
        return namingService.getServicesOfServer(0,10);
    }
}
