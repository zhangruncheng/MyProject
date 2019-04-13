package com.my.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ThreadCallablePool {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(ThreadCallablePool.class);

    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;


    /**
     * 单个任务分多个线程去做，每个线程做对应线程的模块
     * @Author : zhangruncheng
     * @Date : 2019-04-12 12:39
     * @param
     * @return java.util.Map
    **/
    public Map getAllDate() {
        Map resultMap = new HashMap();
        final int size = 5;
        try {
            List<Future<Map<String,Object>>> list = new ArrayList(size);
            /** 分模块操作 */
            for (int i = 0; i < size; i++) {
                Future<Map<String, Object>> mapFuture = executeFuture(i);
                list.add(mapFuture);
            }
            for (int i = 0; i < size; i++) {
                resultMap.putAll(list.get(i).get());

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     *
     * @Author : zhangruncheng
     * @Date : 2019-04-12 12:39
     * @param i
     * @return Future<Map<String,Object>>
    **/
    public Future<Map<String, Object>> executeFuture(int i) {
        return poolTaskExecutor.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return getModelResult(i);
            }
        });
    }

    /**
     * 各模块执行
     * @Author : zhangruncheng
     * @Date : 2019-04-12 12:38
     * @param i
     * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    public Map<String, Object> getModelResult(int i){
        switch (i) {
            case 0:
                new HashMap<>();
                break;
            case 1:
                new HashMap<>();
                break;
                default:break;

        }
        return new HashMap<>();
    }




    public void getT(){
        for (int i = 0; i < 100; i++) {
            poolTaskExecutor.execute(new Work(i));
        }
    }

    static class Work implements Runnable{

        private final int task;

        public Work(final int task){
            this.task = task;
        }

        @Override
        public void run() {

            logger.info(Thread.currentThread().getName() + "第" + task);
        }
    }

}
