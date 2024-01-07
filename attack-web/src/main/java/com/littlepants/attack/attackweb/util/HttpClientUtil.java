package com.littlepants.attack.attackweb.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description Http客户端工具类
 */
public class HttpClientUtil{
    /**
     * form表单提交
     * @param url
     * @param map
     * @return
     */
    public static Map<String,Object> doPostForm(String url, Map<String, Object> map) {
        Map<String,Object> strResult = new HashMap<>();
        // 1. 获取默认的client实例
        CloseableHttpClient client = HttpClients.createDefault();
        // 2. 创建httppost实例
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        // 3. 创建参数队列（键值对列表）
        List<NameValuePair> paramPairs = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object val = map.get(key);
            paramPairs.add(new BasicNameValuePair(key, val.toString()));
        }
        UrlEncodedFormEntity entity;
        try {
            // 4. 将参数设置到entity对象中
            entity = new UrlEncodedFormEntity(paramPairs, "UTF-8");
            System.out.println("封装的参数："+entity);
            // 5. 将entity对象设置到httppost对象中
            httpPost.setEntity(entity);
            // 6. 发送请求并回去响应
            try (CloseableHttpResponse resp = client.execute(httpPost)) {
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                strResult.put("headers", resp.getHeaders("Set-Cookie")[0]);
                strResult.put("entity", EntityUtils.toString(respEntity, "UTF-8"));
            }
            // 9. 关闭响应对象
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 10. 关闭连接，释放资源
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}