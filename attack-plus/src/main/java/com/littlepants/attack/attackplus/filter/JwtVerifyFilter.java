package com.littlepants.attack.attackplus.filter;

import com.littlepants.attack.attackplus.config.RsaKeyProperties;
import com.littlepants.attack.attackplus.constant.CacheKey;
import com.littlepants.attack.attackplus.constant.ConstantKit;
import com.littlepants.attack.attackplus.dto.UserDTO;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.service.UserService;
import com.littlepants.attack.attackplus.utils.jwt.JwtUtil;
import net.oschina.j2cache.CacheChannel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * <p>
 * 鉴权
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */


public class JwtVerifyFilter extends BasicAuthenticationFilter {
    private RsaKeyProperties rsaKeyProperties;
    private CacheChannel cache;
    private UserService userService;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties,
                           CacheChannel cache, UserService userService) {
        super(authenticationManager);
        this.rsaKeyProperties  = rsaKeyProperties;
        this.cache = cache;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header==null||!header.startsWith("AttackToken ")){
            chain.doFilter(request,response);
            return;
        }
        String token = header.replace("AttackToken ","");
        UserDTO userDTO = JwtUtil.getInfoFromToken(token,rsaKeyProperties.getPublicKey(),UserDTO.class).getUserinfo();
        User user = userService.getUserById(userDTO.getId());
        if (!cache.exists(CacheKey.USER,token))
            throw new AccessDeniedException("登录凭证已废弃");
        if (user!=null){
            request.setAttribute("requestUser",user.getUserAcct());
            Authentication authentication = new UsernamePasswordAuthenticationToken
                    (user.getUsername(),null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Long tokenBirthTime = (Long) cache.get(CacheKey.USER,token+user.getUserAcct()).getValue();
            long diff = System.currentTimeMillis()-tokenBirthTime;//时间差
            if (!cache.exists(CacheKey.USER,user.getUsername()))
                throw new AccessDeniedException("登录过期");
            if (diff> ConstantKit.TOKEN_RESET_TIME){
                cache.set(CacheKey.USER,user.getUsername(), ConstantKit.TOKEN_EXPIRE_TIME);
                cache.set(CacheKey.USER,token,ConstantKit.TOKEN_EXPIRE_TIME);
                long newBirthTime = System.currentTimeMillis();
                cache.set(CacheKey.USER,token+user.getUsername(),newBirthTime);
            }
            response.setHeader("Authorization","AttackToken "+token);
            chain.doFilter(request,response);
        }else
            throw new AccessDeniedException("未登录");
    }
}
