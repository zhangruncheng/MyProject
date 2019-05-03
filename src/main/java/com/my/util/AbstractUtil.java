package com.my.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @@uthor : zhangruncheng
 * @Date : 2019-04-17  15:51
 * @Version : 1.0.0
 **/
public abstract class AbstractUtil {


    /**
     * 大集合拆分为 小集合
     * @Author : zhangruncheng
     * @Date : 2019-04-17 16:07
     * @param list
     * @param pageSize
     * @return java.util.List<java.util.List<T>>
    **/
    public static <T> List<List<T>> getGroupArrayList(List<T> list, final int pageSize) {
        if (pageSize < 0 || pageSize > pageSize) {
            throw new IllegalArgumentException("pageSize 不能超过 " + pageSize);
        }
        List<List<T>> objList = new ArrayList<>();
        int size = list.size() / pageSize;
        int count = list.size() % pageSize;

        for (int i = 0; i <= size ; i++) {
            List<T> newObjList = new ArrayList<>();
            if( size == i) {
                for (int j = i * pageSize; j < i * pageSize + count; j++) {
                    newObjList.add(list.get(j));
                }
            } else {
                for (int j = i * pageSize; j < (i + 1) * pageSize; j++) {
                    newObjList.add(list.get(j));
                }
            }
            if (newObjList.size() > 0) {
                objList.add(newObjList);
            }
        }
        return objList;
    }






}
