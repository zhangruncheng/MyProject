package com.forezp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ThreadCountDownLatchPool {

    private static final Logger logger = LoggerFactory.getLogger(ThreadCountDownLatchPool.class);

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;



    public void getData(List<String> list){
        /** 计数器 */
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        /** map数组 */
        Map<String,Map> [] resultList = new Map[list.size()];

        for (int i = 0; i < list.size(); i++) {
            poolTaskExecutor.execute(getSingleDate(list.get(i),countDownLatch,resultList,i));
        }
    }


    /**
     * 单个线程执行
     * @Author : zhangruncheng
     * @Date : 2019-04-12 10:20
     * @param id
     * @param countDownLatch    线程计数器
     * @param resultList        结果集
     * @param resultIdx         结果下标
     * @return java.lang.Runnable
    **/
    public Runnable getSingleDate(String id,CountDownLatch countDownLatch,Map<String,Map> [] resultList,
                                  int resultIdx){
        return new Runnable() {
            @Override
            public void run() {
                Map indexData = null;
                long startTime = System.currentTimeMillis();
                try {
                    /** 获取该任务数据 */
                    indexData = getIndexData();
                }catch (Throwable e) {

                } finally {
                    /** 每执行一次子线程，计数器减1 */
                    countDownLatch.countDown();
                }
                /** 对应线程的数据放入对应的位置 */
                synchronized (resultList) {
                    resultList[resultIdx] = indexData;
                }
                logger.info("getSingleDate--> userTime = {} ms",System.currentTimeMillis() - startTime);
            }
        };
    }

    /**
     * 单个数据
     * @Author : zhangruncheng
     * @Date : 2019-04-11   20:46
     []
     * @return java.util.Map
    **/
   public Map getIndexData(){
        return new HashMap();
   }
}
