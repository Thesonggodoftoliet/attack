package com.littlepants.attack.attackplus.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.config.RsaKeyProperties;
import com.littlepants.attack.attackplus.constant.CacheKey;
import com.littlepants.attack.attackplus.constant.ConstantKit;
import com.littlepants.attack.attackplus.dto.LoginDTO;
import com.littlepants.attack.attackplus.dto.LoginParamDTO;
import com.littlepants.attack.attackplus.dto.UserDTO;
import com.littlepants.attack.attackplus.entity.Role;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.exception.BizException;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.mapper.UserMapper;
import com.littlepants.attack.attackplus.service.UserService;
import com.littlepants.attack.attackplus.service.ValidateCodeService;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import com.littlepants.attack.attackplus.utils.jwt.JwtUtil;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  登录拦截器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final ValidateCodeService validateCodeService;
    private final AuthenticationManager authenticationManager;
    private final RsaKeyProperties rsaKeyProperties;
    private final UserService userService;
    private final CacheChannel cache;
    public JwtLoginFilter(AuthenticationManager authenticationManager,RsaKeyProperties rsaKeyProperties,
                          CacheChannel cache,ValidateCodeService validateCodeService,UserService userService){
        this.authenticationManager = authenticationManager;
        this.rsaKeyProperties = rsaKeyProperties;
        this.cache = cache;
        this.validateCodeService = validateCodeService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginParamDTO loginParamDTO = objectMapper.readValue(request.getInputStream(), LoginParamDTO.class);
            if (validateCodeService.check(loginParamDTO.getKey(),loginParamDTO.getCode()))
                return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginParamDTO.getAccount(),loginParamDTO.getPassword()));
        }catch (BizException e) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                PrintWriter out = response.getWriter();
                out.write(JsonUtil.toString(R.fail(e)));
                out.flush();
                out.close();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }catch (Exception e){
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            e.printStackTrace();
            try {
                PrintWriter out = response.getWriter();
                out.write(JsonUtil.toString(R.fail(ExceptionCode.JWT_USER_INVALID)));
                out.flush();
                out.close();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authentication) {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_acct",authentication.getName()));
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDto(user);
        String token = JwtUtil.generateTokenExpireInMillis(userDTO, rsaKeyProperties.getPrivateKey(), 10*60*1000);
        response.addHeader("Authorization","AttackToken "+token);
        CacheObject cacheObject = cache.get(CacheKey.USER,user.getUserAcct());
        if (cacheObject.getValue()!=null)
            cache.evict(CacheKey.USER,  cacheObject.getValue().toString(),cacheObject.getValue()+user.getUserAcct());
        cache.set(CacheKey.USER,user.getUsername(),token, ConstantKit.TOKEN_EXPIRE_TIME);
        cache.set(CacheKey.USER,token,user.getUserAcct(),ConstantKit.TOKEN_EXPIRE_TIME);
        long currentTime = System.currentTimeMillis();
        cache.set(CacheKey.USER,token+user.getUserAcct(),currentTime);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        List<String> roles = new ArrayList<>();
        for (Role role: (List<Role>)authentication.getAuthorities())
            roles.add(role.getName());
        try {
            PrintWriter out = response.getWriter();
            LoginDTO loginDTO = new LoginDTO(userDTO,token,roles);
            out.write(JsonUtil.toString(R.success(loginDTO)));
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
