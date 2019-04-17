package com.my.thread;

import com.my.util.AbstractUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-17  21:03
 * @Version : 1.0.0
 **/
@Service
public class ThreadTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadTest.class);

    @Resource
    private ThreadPoolTaskExecutor threadPool;


    /**
     * 同步线程测试
     * 主线程等待，自线程走完，主线程启动
     * @Author : zhangruncheng
     * @Date : 2019-04-17 22:20
     * @param
     * @return void
    **/
    public void threadSynchronization(){
        List<List<Integer>> groupArrayList = AbstractUtil.getGroupArrayList(getList(10050), 1000);
        CountDownLatch countDownLatch = new CountDownLatch(groupArrayList.size());
        logger.info("threadSynchronization --> start list size is [{}]" , groupArrayList.size());
        for (List<Integer> integers : groupArrayList) {
            /** 同步zuse */
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 100; i++) {
                            int j =0;
                            j +=i;
                        }
                        Thread.sleep(500);
                        logger.info("threadSynchronization：run --> first start is [{}}  and List size is [{}]",integers.get(0),integers.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("threadSynchronization --> 所有线程执行完");
        logger.info("threadSynchronization --> end");
    }

    /**
     * 异步线程
     * @Author : zhangruncheng
     * @Date : 2019-04-17 22:22
     * @param
     * @return void
    **/
    public void asynchronousThread(){
        List<List<Integer>> groupArrayList = AbstractUtil.getGroupArrayList(getList(10050), 1000);
        CountDownLatch countDownLatch = new CountDownLatch(groupArrayList.size());
        logger.info("asynchronousThread --> start list size is [{}]" , groupArrayList.size());
        for (List<Integer> integers : groupArrayList) {
            /** 异步 */
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("asynchronousThread：run --> first start is [{}}  and List size is [{}]",integers.get(0),integers.size());
                }
            });
        }

        logger.info("asynchronousThread --> 所有线程执行完");
        logger.info("asynchronousThread --> end");
    }


    private List<Integer> getList(final int size){
        List<Integer> list = new ArrayList();
        for (int i = 0; i <= size ; i++) {
            list.add(i);
        }
        return list;
    }


}
