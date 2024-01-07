package com.littlepants.attack.attackplus.config;

import com.littlepants.attack.attackplus.filter.JwtLoginFilter;
import com.littlepants.attack.attackplus.filter.JwtVerifyFilter;
import com.littlepants.attack.attackplus.handler.CustomAuthenticationEntryPoint;
import com.littlepants.attack.attackplus.handler.CustomLogoutHandler;
import com.littlepants.attack.attackplus.handler.UserAuthAccessDeniedHandler;
import com.littlepants.attack.attackplus.service.ValidateCodeService;
import com.littlepants.attack.attackplus.service.impl.UserServiceImpl;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 *  安全配置类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final UserServiceImpl userServiceImpl;
    private final RsaKeyProperties rsaKeyProperties;
    private final CacheChannel cache;
    private final ValidateCodeService validateCodeService;

    public SecurityConfig(UserServiceImpl userServiceImpl, RsaKeyProperties rsaKeyProperties, CacheChannel cache, ValidateCodeService validateCodeService) {
        this.userServiceImpl = userServiceImpl;
        this.rsaKeyProperties = rsaKeyProperties;
        this.cache = cache;
        this.validateCodeService = validateCodeService;
    }

    private String[] loadExcludePath(){
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
    public BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userServiceImpl)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager,HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(loadExcludePath()).permitAll()
                .antMatchers("/topology/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/user/captcha").permitAll()
                .antMatchers("/case/updateFromCaldera").permitAll()
                .antMatchers("/swagger-ui/**","/webjars/**","/swagger-resources/**","/v2/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new JwtLoginFilter(authenticationManager,rsaKeyProperties,cache,validateCodeService,userServiceImpl))
                .addFilter(new JwtVerifyFilter(authenticationManager,rsaKeyProperties,cache,userServiceImpl))
                .exceptionHandling().accessDeniedHandler(new UserAuthAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .addLogoutHandler(new CustomLogoutHandler())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return httpSecurity.build();
    }
}
