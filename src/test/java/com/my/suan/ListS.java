package com.my.suan;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListS {

    public static void main(String[] args) {

        double [][] excel = new double[200][5];

        excel[0][0] = 1.0;
        excel[0][1] = 28.0;
        excel[0][2] = 1.0;
        excel[0][3] = 0.1;
        for (int i = 1; i < 200; i++) {
            excel[i][0] = excel[i-1][0] + 1;
            excel[i][1] = excel[i-1][1] + (double) 1/12;
            excel[i][2] = excel[i-1][2] + 1;
            excel[i][3] = excel[i][2] - excel[i-1][3];
            double sum = 0;
            for (int j = 0,length = excel[i].length; j < length; j++) {
                sum += excel[i][j];
            }
            excel[i][4] = sum;
        }

        List<Double> list = new ArrayList<>();
        for (int i = 0; i < excel.length; i++) {
            list.add(excel[i][4]);
        }
        Double min = Collections.min(list);

        System.out.println(JSON.toJSONString(excel));

    }

}
