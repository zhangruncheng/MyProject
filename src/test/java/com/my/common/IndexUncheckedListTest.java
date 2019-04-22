package com.my.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-22  10:53
 * @Version : 1.0.0
 **/
public class IndexUncheckedListTest {

    public static void main(String[] args) {
        List<String> list = new IndexUncheckedList<>();

        Map<String,String> map= new HashMap<>();
        String s1 = map.get("");
        BigDecimal b = new BigDecimal(s1);
        b.longValue();

        list.add(5, "555");
        list.add(7, "777");
        list.add(2, "222");
        list.add(9, "999");
        list.add(1, "111");
        System.out.println("isEmpty:" + list.isEmpty());
        System.out.println("size :" + list.size());

        System.out.println("++++++++++++++++++++++++++++++");

        for (String s : list) {
            System.out.println(s);
        }

        System.out.println("==========================");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}