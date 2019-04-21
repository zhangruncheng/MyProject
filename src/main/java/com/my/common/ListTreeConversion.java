package com.my.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-20  09:55
 * @Version : 1.0.0
 **/
public class ListTreeConversion {


    public static <T> List<TreeNode<T>> buildTreeLoop(List<TreeNode<T>> data, String rootPid, String idVarName,
                                                      String PidVarName,String descVarName){
        List<TreeNode<T>> list = new ArrayList<>();
//        data.forEach();
        return null;
    }

    /**
     * 根据变量名 得到变量属性
     * @Author : zhangruncheng
     * @Date : 2019-04-20 10:00
     * @param dataItem
     * @param varName
     * @return java.lang.String
    **/
    private static String getFieldValue(Object dataItem, String varName) {
        try {
            Field field = dataItem.getClass().getDeclaredField(varName);
            field.setAccessible(true);
            Object value = field.get(dataItem);
            if (null == value) {
                throw new IllegalArgumentException("null value :" + varName);
            }
            return String.valueOf(value);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("illeagel field name :" + varName);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("illeagel field name :" + varName);
        }
    }

    private static Map getFieldMap(Object obj, String varName) {
        Map map = new HashMap();
        Field declaredField = null;
        try {
            declaredField = obj.getClass().getDeclaredField(varName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        map.put(varName,declaredField);
        return map;
    }

    private static String getFieldValue(Object obj, String varName, Map<String, Field> map) {
        Field field = map.get(varName);
        field.setAccessible(true);
        try {
            Object value = field.get(obj);
            if (null == value) {
                throw new IllegalArgumentException("null value : " + varName);
            }
            return String.valueOf(value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("illegal filed name : " + varName);
        }
    }

    private static String getVarValue(Object obj, String varName, Map<String, Field> fieldMap) {
        if (null == fieldMap) {
            fieldMap = getFieldMap(obj,varName);
        }
        return getFieldValue(obj,varName,fieldMap);
    }

}
