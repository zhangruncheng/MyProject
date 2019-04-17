package com.my.service;

import com.my.annotation.CheckNull;
import com.my.bean.Teacher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-17  16:12
 * @Version : 1.0.0
 **/
@Service("baseService")
public class BaseServiceImpl implements BaseService {

    @CheckNull
    @Override
    public List get(@CheckNull(group = "test") Teacher teacher) {
        if (null != teacher) {
            return new ArrayList();
        }

        return null;
    }
}
