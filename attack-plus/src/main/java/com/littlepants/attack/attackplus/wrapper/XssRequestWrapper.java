package com.littlepants.attack.attackplus.wrapper;

import org.owasp.validator.html.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;
import java.util.Objects;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/11
 * @description Xss过滤包装类
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {
    //指定策略文件
    private static String antiSamyPath = Objects.requireNonNull(XssRequestWrapper.class.getClassLoader()
            .getResource("antisamy.xml")).getFile();
    public static Policy policy = null;
    static {
        try {
            policy =  Policy.getInstance(antiSamyPath);
        }catch (PolicyException e){
            e.printStackTrace();
        }
    }

    /**
     * AntiSamy过滤数据
     * @param taintedHTML 需要进行过滤的数据
     * @return 返回过滤后的数据
     */
    private String xssClean(String taintedHTML){
        try {
            AntiSamy antiSamy = new AntiSamy();
            CleanResults cleanResults = antiSamy.scan(taintedHTML,policy);
        }catch (ScanException | PolicyException e){
            e.printStackTrace();
        }
        return taintedHTML;
    }

    public XssRequestWrapper(HttpServletRequest request){
        super(request);
    }

    @Override
    public String[] getParameterValues(String name){
        String[] values = super.getParameterValues(name);
        if (values == null)
            return null;
        int len = values.length;
        String[] newArray = new String[len];
        for (int i=0;i<len;i++){
            newArray[i] = xssClean(values[i]);
        }
        return newArray;
    }

    @Override
    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (str == null) {
            return null;
        }
        return xssClean(str);
    }

    @Override
    public String getHeader(String paramString) {
        String str = super.getHeader(paramString);
        if (str == null) {
            return null;
        }
        return xssClean(str);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> requestMap = super.getParameterMap();
        for (Map.Entry<String, String[]> me : requestMap.entrySet()) {
            String[] values = me.getValue();
            for (int i = 0; i < values.length; i++) {
                values[i] = xssClean(values[i]);
            }
        }
        return requestMap;
    }
}
