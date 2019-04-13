package com.my.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseDao {

    /**
     * 批量查询
     * @Author : zhangruncheng
     * @Date : 2019-04-13 13:09
     * @param
     * @return java.util.List
    **/
    List getList();

    /**
     * 批量插入  使用序列
     * @Author : zhangruncheng
     * @Date : 2019-04-13 13:09
     * @param list
     * @return java.lang.Integer
    **/
    Integer addListBySequrence(@Param("list") List list);

    /**
     * 有责更新，无则插入
     * @Author : zhangruncheng
     * @Date : 2019-04-13 13:11
     * @param list
     * @return java.lang.Integer
    **/
    Integer updateOrIAdd(@Param("list") List list);

    /**
     * 根据map 传参数
     * @Author : zhangruncheng
     * @Date : 2019-04-13 17:13
     * @param map
     * @return java.lang.Object
    **/
    Object getBeanByMap(@Param("map") Map map);

    /**
     * 多个传参
     * @Author : zhangruncheng
     * @Date : 2019-04-13 17:16
     * @param list
     * @param bean
     * @return java.util.List
    **/
    List getBean(@Param("list") List<String> list, @Param("bean") Object bean);

    /**
     * 一对一，一对多
     * @Author : zhangruncheng
     * @Date : 2019-04-13 17:20
     * @param map
     * @return java.util.List
    **/
    List getBeanList(Map map);
}
