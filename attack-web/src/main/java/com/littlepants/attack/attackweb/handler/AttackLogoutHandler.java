package com.littlepants.attack.attackweb.handler;

import com.littlepants.attack.attackweb.config.RsaKeyProperties;
import com.littlepants.attack.attackweb.entity.AccessLog;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.service.intf.LogService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AttackLogoutHandler implements LogoutHandler {
    private final JedisPool jedisPool;
    private final RsaKeyProperties rsaKeyProperties;
    private final LogService logService;
    public AttackLogoutHandler(JedisPool jedisPool, RsaKeyProperties rsaKeyProperties,LogService logService){
        this.jedisPool = jedisPool;
        this.rsaKeyProperties = rsaKeyProperties;
        this.logService = logService;
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");
        String token = header.replace("AttackToken ","");
        User userInfo = JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(),User.class).getUserinfo();
        Jedis jedis = jedisPool.getResource();
        jedis.del(userInfo.getUsername(),token,token+userInfo.getUsername());
        AccessLog accessLog = new AccessLog();
        accessLog.setUserName(userInfo.getUsername());
        accessLog.setLogEvent("退出");
        accessLog.setRemoteAddress(request.getRemoteAddr());
        logService.addLog(accessLog);
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=utf-8;");
            PrintWriter printWriter = response.getWriter();
            Map<String,Object> map = new HashMap<>();
            map.put("code",HttpServletResponse.SC_OK);
            map.put("msg","退出成功");
            printWriter.write(JsonUtils.toString(map));
            printWriter.flush();
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
