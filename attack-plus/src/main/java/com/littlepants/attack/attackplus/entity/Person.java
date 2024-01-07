package com.littlepants.attack.attackplus.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/2/16
 * @description
 */
public class Person implements Serializable {
    private String name;
    private int age;
    private Character character;
    private char aChar;
    private List<Integer> list;
    private Map<String,Integer> map;
    private ArrayList<Integer> arrayList;

    public Person(String name,int age){
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
