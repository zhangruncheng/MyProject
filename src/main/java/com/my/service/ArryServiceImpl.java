package com.my.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArryServiceImpl implements ArryService {


    ThreadLocal threadLocal = new ThreadLocal();


    private void setArry(Integer rows){
        double[][] excel = new double[rows][5];

        excel[0][0] = 1.0;
        excel[0][1] = 28.0;
        excel[0][2] = 1.0;
        excel[0][3] = 0.1;
        for (int i = 1; i < rows; i++) {
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
        threadLocal.set(excel);
    }

    @Override
    public String getDate(Integer rows) {
        setArry(rows);
        double[][] doubles = (double[][]) threadLocal.get();
        log.info("getDate--> get this two arry rows = [{}], jsonStr = [{}]",rows,doubles.length);
        if (rows != doubles.length) {
            log.error("getDate--> error [{}]",rows);
        }
        return JSON.toJSONString(doubles);
    }
}
