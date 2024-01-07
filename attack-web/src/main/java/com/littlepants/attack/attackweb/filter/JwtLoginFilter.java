package com.littlepants.attack.attackweb.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlepants.attack.attackweb.config.ConstantKit;
import com.littlepants.attack.attackweb.config.RsaKeyProperties;
import com.littlepants.attack.attackweb.entity.AccessLog;
import com.littlepants.attack.attackweb.entity.PolicyGroup;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.service.intf.LogService;
import com.littlepants.attack.attackweb.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final LogService logService;
    private final AuthenticationManager authenticationManager;
    private final RsaKeyProperties rsaKeyProperties;
    private final JedisPool jedisPool;
    public JwtLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties,
                          JedisPool jedisPool,LogService logService){
        this.authenticationManager = authenticationManager;
        this.rsaKeyProperties = rsaKeyProperties;
        this.jedisPool = jedisPool;
        this.logService = logService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        User user = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            user = objectMapper.readValue(request.getInputStream(),User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.userPassword()
                    )
            );
        } catch (Exception e) {
            try {
                AccessLog accessLog = new AccessLog();
                if (user!=null) {
                    accessLog.setUserName(user.getUsername());
                    accessLog.setLogEvent("登录被拒绝");
                    accessLog.setRemoteAddress(request.getRemoteAddr());
                    logService.addLog(accessLog);
                }
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                Map<String, Object> map = new HashMap<>();
                map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                map.put("message", "账号或密码错误！");
                out.write(new ObjectMapper().writeValueAsString(map));
                out.flush();
                out.close();
                e.printStackTrace();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authentication){
        User user = new User();
        user.setUsername(authentication.getName());
        user.setGroup_id((List<PolicyGroup>) authentication.getAuthorities());
        String token = JwtUtils.generateTokenExpireInMillis(user,rsaKeyProperties.getPrivateKey(),10*60*1000);
        Jedis jedis = jedisPool.getResource();
        response.addHeader("Authorization","AttackToken "+token);
        String oldToken = jedis.get(user.getUsername());
        if (oldToken!=null)
            jedis.del(oldToken,oldToken+user.getUsername());
        jedis.set(user.getUsername(),token);
        jedis.expire(user.getUsername(), ConstantKit.TOKEN_EXPIRE_TIME);
        jedis.set(token,user.getUsername());
        jedis.expire(token,ConstantKit.TOKEN_EXPIRE_TIME);
        long currentTime = System.currentTimeMillis();
        jedis.set(token+user.getUsername(), Long.toString(currentTime));
        jedis.close();
        AccessLog accessLog = new AccessLog();
        accessLog.setLogEvent("登陆成功");
        accessLog.setUserName(user.getUsername());
        accessLog.setRemoteAddress(request.getRemoteAddr());
        logService.addLog(accessLog);
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = response.getWriter();
            Map<String,Object> map = new HashMap<>(4);
            map.put("code",HttpServletResponse.SC_OK);
            map.put("message","登陆成功！");
            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
