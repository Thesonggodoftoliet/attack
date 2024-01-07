package com.littlepants.attack.attackplus.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 验证码
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/23
 */

public interface ValidateCodeService {
    void generate(String key, HttpServletResponse response) throws IOException;

    boolean check(String key,String value);
}
