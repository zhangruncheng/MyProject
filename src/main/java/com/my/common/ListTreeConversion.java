package com.my.common;

import org.springframework.util.CollectionUtils;

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


    /**
     * 循环建树
     * @Author : zhangruncheng
     * @Date : 2019-04-22 10:23
     * @param data
     * @param rootPid
     * @param idVarName
     * @param PidVarName
     * @param descVarName
     * @return java.util.List<com.my.common.TreeNode<T>>
    **/
    public static <T> List<TreeNode<T>> buildTreeLoop(List<TreeNode<T>> data, String rootPid, String idVarName,
                                                      String PidVarName, String descVarName) {
        List<TreeNode<T>> list = new ArrayList<>();
        data.forEach(obj -> {
            String id = getFieldValue(obj.getData(), idVarName);
            String pid = getFieldValue(obj.getData(), PidVarName);
            obj.setPid(pid);
            obj.setId(id);
            if (rootPid.equals(pid)) {
                list.add(obj);
            }
            data.forEach(t -> {
                String ppid = getFieldValue(t.getData(), PidVarName);
                if (ppid.equals(id)) {
                    if (null == obj.getChildrenList()) {
                        obj.setChildrenList(new ArrayList<>());
                    }
                    obj.getChildrenList().add(t);
                }
            });
        });
        return list;
    }

    public static <T> List<TreeNode<T>> buildTreeByLoop(List<TreeNode<T>> data, String rootPid,
                                                        String idVarName, String pidVarName, String descVarName) {
        Map map = null;
        List<TreeNode<T>> list = new ArrayList<>();
        data.forEach(s -> {
            String id = getVarValue(s.getData(), idVarName, map);
            String pid = getVarValue(s.getData(), pidVarName, map);
            s.setId(id);
            s.setPid(pid);
            if (rootPid.equals(pid)) {
                list.add(s);
            }
            data.forEach(t -> {
                String ppid = getVarValue(t.getData(), pidVarName, map);
                if (ppid.equals(id)) {
                    if (null == s.getChildrenList()) {
                        s.setChildrenList(new ArrayList<>());
                    }
                    s.getChildrenList().add(t);
                }
            });
        });
        return list;
    }

    /**
     * 递归解树
     * @Author : zhangruncheng
     * @Date : 2019-04-22 10:23
     * @param treeList
     * @param paramList
     * @return void
    **/
    public static <T> void treeToList(List<TreeNode<T>> treeList, List<T> paramList){
        treeList.forEach(var -> {
            if (null == var) {
                return;
            }
            paramList.add(var.getData());
            if (CollectionUtils.isEmpty(var.getChildrenList())) {
                return;
            }
            treeToList(var.getChildrenList(),paramList);
        });
    }

    /**
     * 根据变量名 得到变量属性
     *
     * @param dataItem
     * @param varName
     * @return java.lang.String
     * @Author : zhangruncheng
     * @Date : 2019-04-20 10:00
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
        map.put(varName, declaredField);
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
            fieldMap = getFieldMap(obj, varName);
        }
        return getFieldValue(obj, varName, fieldMap);
    }

}
