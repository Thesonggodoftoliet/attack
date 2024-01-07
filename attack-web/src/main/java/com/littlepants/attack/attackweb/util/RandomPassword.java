package com.littlepants.attack.attackweb.util;

import java.util.Random;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/9/22
 * @description 随机密码类
 */
public class RandomPassword {
    /**
     * 生成随机密码
     * @return
     */
    public static String generateRandomPassword(){
        char[] password = new char[10];
        Random random = new Random();
        int character = random.nextInt(15)+33;
        int number = random.nextInt(10)+48;
        int upperCase = random.nextInt(26)+65;
        int lowerCase = random.nextInt(26)+97;
        password[0] =(char) upperCase;
        password[1] = (char) lowerCase;
        password[2] = (char) number;
        password[3] = (char) character;
        for (int i=4;i<10;i++){
            int choice = random.nextInt(4);
            switch (choice){
                case 0:password[i]=(char) (random.nextInt(26)+65);break;
                case 1:password[i]=(char) (random.nextInt(26)+97);break;
                case 2:password[i]=(char) (random.nextInt(10)+48);break;
                case 3:password[i]=(char) (random.nextInt(15)+33);break;
            }
        }
        return new String(password);
    }
}
