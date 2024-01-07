package com.littlepants.attack.attackweb.filter;

import com.littlepants.attack.attackweb.config.ConstantKit;
import com.littlepants.attack.attackweb.config.RsaKeyProperties;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class JwtVerifyFilter extends BasicAuthenticationFilter {
    private final RsaKeyProperties rsaKeyProperties;
    private final JedisPool jedisPool;

    public JwtVerifyFilter(AuthenticationManager authenticationManager,RsaKeyProperties rsaKeyProperties,
                           JedisPool jedisPool) {
        super(authenticationManager);
        this.rsaKeyProperties = rsaKeyProperties;
        this.jedisPool = jedisPool;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null||!header.startsWith("AttackToken ")){
            //不应该在此处抛出异常，抛出异常会重定向至/error，但/error并未放通
            //故会出现只要是出现了异常，如404等都会被AuthenticationEntryPoint捕获从而返回401
//            throw new AccessDeniedException("未登录");
            chain.doFilter(request,response);
            return;
        }
        String token = header.replace("AttackToken ","");
        User user = JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(),User.class).getUserinfo();
        Jedis jedis = jedisPool.getResource();
        request.setAttribute("requestUser",user.getUsername());
        if (jedis.get(token) == null)
            throw new AccessDeniedException("登录凭证已废弃");
        if (user!=null){
            Authentication authentication = new UsernamePasswordAuthenticationToken
                    (user.getUsername(),null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            long tokenBirthTime = Long.parseLong(jedis.get(token + user.getUsername()));
            logger.info("token birth time is:"+tokenBirthTime);
            long diff = System.currentTimeMillis()-tokenBirthTime;//时间差
            logger.info("token has existed for:"+diff);
            if (jedis.get(user.getUsername())==null)
                throw new AccessDeniedException("登录过期");
            if (diff> ConstantKit.TOKEN_RESET_TIME){
                jedis.expire(user.getUsername(), ConstantKit.TOKEN_EXPIRE_TIME);
                jedis.expire(token,ConstantKit.TOKEN_EXPIRE_TIME);
                logger.info("Reset expire time success");
                long newBirthTime = System.currentTimeMillis();
                jedis.set(token+user.getUsername(), Long.toString(newBirthTime));
            }
            jedis.close();
//            response.setHeader("Authorization","AttackToken "+token);
            chain.doFilter(request,response);
        }else {
            throw new AccessDeniedException("未登录");
        }
    }
}
