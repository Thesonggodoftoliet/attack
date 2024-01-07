package com.littlepants.attack.attackplus.filter;


import com.littlepants.attack.attackplus.wrapper.XssRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/11
 * @description
 */
public class XssFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        filterChain.doFilter(new XssRequestWrapper(request),servletResponse);
    }
}
