package com.my.service;

import com.my.annotation.CheckNull;
import com.my.bean.Teacher;

import java.util.List;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-17  16:10
 * @Version : 1.0.0
 **/
public interface BaseService {

    List get(@CheckNull(group = "test") Teacher teacher);

    List getList();

}
