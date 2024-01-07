package com.littlepants.attack.attackplus.handler;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.config.RsaKeyProperties;
import com.littlepants.attack.attackplus.constant.CacheKey;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import com.littlepants.attack.attackplus.utils.jwt.JwtUtil;
import lombok.Setter;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * 登出
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */

@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Setter
    private CacheChannel cache;
    @Setter
    private RsaKeyProperties rsaKeyProperties;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");
        String token = header.replace("AttackToken ","");
        User userInfo = JwtUtil.getInfoFromToken(token, rsaKeyProperties.getPublicKey(),User.class).getUserinfo();
        cache.evict(CacheKey.USER,userInfo.getUsername(),token,token+userInfo.getUsername());
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=utf-8;");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JsonUtil.toString(R.success(null,"成功退出")));
            printWriter.flush();
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
