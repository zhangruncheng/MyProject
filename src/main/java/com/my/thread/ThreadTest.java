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
     *
     * @param
     * @return void
     * @Author : zhangruncheng
     * @Date : 2019-04-17 22:20
     **/
    public void threadSynchronization() {
        List<List<Integer>> groupArrayList = AbstractUtil.getGroupArrayList(getList(10050), 1000);
        CountDownLatch countDownLatch = new CountDownLatch(groupArrayList.size());
        logger.info("threadSynchronization --> start list size is [{}]", groupArrayList.size());
        Integer integer = new Integer(0);
        for (List<Integer> integers : groupArrayList) {
            /** 同步zuse */
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int i = 6;
                        double j = 10 / i;
                        logger.info("threadSynchronization：run --> 计算结果 = [{}]", j);
                        ;
                        Thread.sleep(500);
                        logger.info("threadSynchronization：run --> first start is [{}}  and List size is [{}]", integers.get(0), integers.size());
                    } catch (Exception e) {
                        logger.info("threadSynchronization run --> Exception ", e);
                        throw new IllegalArgumentException(countDownLatch.getCount() + "异常");
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
     *
     * @param
     * @return void
     * @Author : zhangruncheng
     * @Date : 2019-04-17 22:22
     **/
    public void asynchronousThread() {
        List<List<Integer>> groupArrayList = AbstractUtil.getGroupArrayList(getList(10050), 1000);
        CountDownLatch countDownLatch = new CountDownLatch(groupArrayList.size());
        logger.info("asynchronousThread --> start list size is [{}]", groupArrayList.size());
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
                    logger.info("asynchronousThread：run --> first start is [{}}  and List size is [{}]", integers.get(0), integers.size());
                }
            });
        }

        logger.info("asynchronousThread --> 所有线程执行完");
        logger.info("asynchronousThread --> end");
    }


    private List<Integer> getList(final int size) {
        List<Integer> list = new ArrayList();
        for (int i = 0; i <= size; i++) {
            list.add(i);
        }
        return list;
    }


    public static void main(String[] args) {
        Object object = new Object();
        for (int i = 1; i <= 5; i++) {
            new Thread(new MyThread(object, i)).start();
        }
    }


}

class MyThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MyThread.class);
    private static int index = 0;

    private ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();
    private static int count=0;//计数 三次一轮回
    private Object obj;
    private int n;//接参 i值


    MyThread(Object obj, int n) {
        this.obj = obj;
        this.n = n;
    }


    @Override
    public void run() {
        /** 线程上锁，互不影响 */
        synchronized (obj) {
            while (index < 100) {//i++ 在代码块 所以到74就可以了

                obj.notifyAll();//唤醒所有线程

                if (count % 3 != (n - 1)) { //找出下一个线程  不正确的线程等待

                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                index++;
                System.out.println(Thread.currentThread().getName() + ": " + index);
                if (index % 5 == 0) { //打印了五次后 此线程让出资源，等待
                    try {
                        count++; //count是static修饰 ，为了共享
                        System.out.println();
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
