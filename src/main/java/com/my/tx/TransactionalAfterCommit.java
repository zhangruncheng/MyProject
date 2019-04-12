package com.forezp.tx;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;


public class TransactionalAfterCommit {

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;



    @Transactional(rollbackFor = Exception.class )
    public void addDatabase(){


        addData();

        final String id = "str";
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            /** 事务提交之后运行 */
            @Override
            public void afterCommit() {
                if (StringUtils.isNotBlank(id)){
                    poolTaskExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            /** 运行方法 */
                        }
                    });
                }
            }
        });
    }

    public void addData(){

    }
}
