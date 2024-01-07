package com.littlepants.attack.attackplus.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/2/4
 * @description 将Class转为Map及其他Class处理类
 */
public class ClassUtil {

    /**
     * 对象转Map
     * @param object 对象
     * @return 返回Map
     * @throws IllegalAccessException 无法访问成员
     */
    public static Map beanToMap(Object object) throws IllegalAccessException {
        Map<String,Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field:fields){
            map.put(field.getName(),field.get(object));
        }
        return map;
    }

    public static <T> T mapToBean(Map map, Class<T> beanClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        T object = beanClass.getDeclaredConstructor().newInstance();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field:fields){
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod)||Modifier.isFinal(mod))
                continue;
            if (map.containsKey(field.getName()))
                field.set(object, map.get(field.getName()));
        }
        return object;
    }

    /**
     * 判断类是是不是基本类型及其包装类
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz){
        try {
            if (clazz.isPrimitive())
                return true;
            return ((Class<?>)clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalArgumentException|NoSuchFieldException | IllegalAccessException |SecurityException e ) {
            return false;
        }
    }

    /**
     * 判断类是不是自定义类，是返回true
     * @param clazz
     * @return
     */
    public static boolean isCustomer(Class<?> clazz){
        if (isPrimitiveOrWrapper(clazz))
            return false;
        return clazz != List.class && clazz != Map.class && clazz != String.class && clazz != Set.class;
    }
}
