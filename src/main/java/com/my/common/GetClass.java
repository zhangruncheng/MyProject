package com.my.common;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-25  21:35
 * @Version : 1.0.0
 **/
public class GetClass {

    /**
     * obj 为需要的具体类型对象
     * @Author : zhangruncheng
     * @Date : 2019-04-25 21:42
     * @param obj
     * @return java.util.Map<java.lang.String,java.lang.String>
    **/
    public static Map<String, String> getFiledMap(Class clazz) {
        Map<String,String> map = new HashMap<>();
        /** obj 为需要的具体类型对象 分页的话要具体到对应的LIst 属性 */
//        Class<?> clazz = obj.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (null != clazz) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        fieldList.forEach(field -> map.put(field.getName(),field.getType().getTypeName()));
        return map;
    }
}
