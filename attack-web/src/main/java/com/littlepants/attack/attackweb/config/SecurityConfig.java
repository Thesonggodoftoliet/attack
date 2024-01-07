package com.littlepants.attack.attackweb.config;

import com.littlepants.attack.attackweb.filter.JwtLoginFilter;
import com.littlepants.attack.attackweb.filter.JwtVerifyFilter;
import com.littlepants.attack.attackweb.handler.AttackLogoutHandler;
import com.littlepants.attack.attackweb.handler.CustomAuthenticationEntryPoint;
import com.littlepants.attack.attackweb.handler.UserAuthAccessDeniedHandler;
import com.littlepants.attack.attackweb.service.implement.UserServiceImpl;
import com.littlepants.attack.attackweb.service.intf.LogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import redis.clients.jedis.JedisPool;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/10/12
 * @description
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final UserServiceImpl userServiceImpl;
    private final RsaKeyProperties rsaKeyProperties;
    private final JedisPool jedisPool;
    private final LogService logService;

    public SecurityConfig(UserServiceImpl userServiceImpl, RsaKeyProperties rsaKeyProperties, JedisPool jedisPool, LogService logService) {
        this.userServiceImpl = userServiceImpl;
        this.rsaKeyProperties = rsaKeyProperties;
        this.jedisPool = jedisPool;
        this.logService = logService;
    }

    private String[] loadExcludePath() {
        return new String[]{
                "/static/**",
                "/templates/**",
                "/img/**",
                "/js/**",
                "/css/**",
                "/lib/**"
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ProviderManager providerManager(){
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userServiceImpl);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(daoAuthenticationProvider);
//    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(){
//        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        authenticationProvider.setUserDetailsService(userServiceImpl);
//        return authenticationProvider;
//    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userServiceImpl)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager, HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                //放通所有静态资源
                .antMatchers(loadExcludePath()).permitAll()
                //放通接口文档
                .antMatchers("/swagger-ui.html","/webjars/**","/swagger-resources/**","/v2/**").permitAll()
                //放通注册
                .antMatchers(HttpMethod.POST,"/user/add").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/logger/**").hasAnyRole("ADMIN","LOGGER")
                .antMatchers("/user/**").hasAnyRole("ADMIN","LOGGER","USER")
                //其余请求都需要认证后访问
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JwtLoginFilter(authenticationManager,rsaKeyProperties, jedisPool,logService))
                .addFilter(new JwtVerifyFilter(authenticationManager, rsaKeyProperties,jedisPool))
                //已认证但是权限不够
                .exceptionHandling().accessDeniedHandler(new UserAuthAccessDeniedHandler())
                .and()
                //未能通过认证，也就是未登录
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .addLogoutHandler(new AttackLogoutHandler(jedisPool,rsaKeyProperties,logService))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//禁用session
        return httpSecurity.build();
    }
}
