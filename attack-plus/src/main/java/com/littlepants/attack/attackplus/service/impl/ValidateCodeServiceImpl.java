package com.littlepants.attack.attackplus.service.impl;

import com.littlepants.attack.attackplus.constant.CacheKey;
import com.littlepants.attack.attackplus.exception.BizException;
import com.littlepants.attack.attackplus.service.ValidateCodeService;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 验证码实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    private final CacheChannel channel;

    public ValidateCodeServiceImpl(CacheChannel channel) {
        this.channel = channel;
    }

    /**
     * 生成验证码
     * @param key 生成 key
     * @param response 返回流
     * @throws IOException IOException
     */
    @Override
    public void generate(String key, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(key))
            throw BizException.validFail("验证码key不能为空");

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setHeader(HttpHeaders.PRAGMA,"No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL,"No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES,0L);

        Captcha captcha = new ArithmeticCaptcha(115,42);
        captcha.setCharType(2);

        channel.set(CacheKey.CAPTCHA,key,StringUtils.lowerCase(captcha.text()));
        captcha.out(response.getOutputStream());
    }

    /**
     * 校验验证码
     * @param key  前端的key
     * @param value 前端的待检验值
     * @return boolean
     */
    @Override
    public boolean check(String key, String value) {
        if (StringUtils.isBlank(value))
            throw BizException.validFail("请输入验证码");
        CacheObject cacheObject = channel.get(CacheKey.CAPTCHA,key);
        if (cacheObject.getValue()==null)
            throw BizException.validFail("验证码已过期");
        if (!StringUtils.equalsIgnoreCase(value,String.valueOf(cacheObject.getValue())))
            throw BizException.validFail("验证码不正确");
        //丢弃验证码
        channel.evict(CacheKey.CAPTCHA,key);
        return true;
    }
}
