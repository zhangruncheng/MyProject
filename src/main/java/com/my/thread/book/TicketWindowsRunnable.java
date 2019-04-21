package com.my.thread.book;

import javax.validation.constraints.Max;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-21  14:31
 * @Version : 1.0.0
 **/
public class TicketWindowsRunnable implements Runnable {



    private int index = 1;

    private final static int  MAX = 500;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while ( index <= MAX) {
            System.out.println(Thread.currentThread() + " 的号码是 " + (index ++));
        }
    }
}
