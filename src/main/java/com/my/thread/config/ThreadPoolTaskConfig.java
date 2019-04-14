package com.my.thread.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class ThreadPoolTaskConfig {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTaskConfig.class);

    @Bean
    public ThreadPoolTaskExecutor executor(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();
        logger.info("核心 = " +core);
        //设置核心线程数
        executor.setCorePoolSize(4);
        //设置最大线程数
        executor.setMaxPoolSize(20);
        //除核心线程外的线程存活时间
        executor.setKeepAliveSeconds(200);
        //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(40);
        //线程名称前缀
        executor.setThreadNamePrefix("thread-execute");
        //设置拒绝策略


        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
