package com.littlepants.attack.attackplus.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/2/28
 * @description 用于处理 fscan nessus等的文件用于构建网络
 */
public class NetworkProcessUtil {
    public static void Nessus(String path){
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(NetworkProcessUtil.class.getClassLoader().getResource("static/response.json"));
            Element  root = document.getRootElement();
            System.out.println();

        }catch (DocumentException e){
            e.printStackTrace();
        }
    }

    public static void Nessus(File file){

    }
}
