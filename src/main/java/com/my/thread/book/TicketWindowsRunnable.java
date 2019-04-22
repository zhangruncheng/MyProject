package com.my.thread.book;


import com.my.bean.Student;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-21  14:31
 * @Version : 1.0.0
 **/
public class TicketWindowsRunnable implements Runnable {



    private int index = 1;

    private final static int  MAX = 500;

    private final static Object mutes = new Object();

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
        synchronized (mutes) {
            while (index <= MAX) {
                System.out.println(Thread.currentThread() + " 的号码是 " + (index++));
            }
        }
    }

    public static void main(String[] args) {


        final TicketWindowsRunnable task = new TicketWindowsRunnable();
        Thread thread1 = new Thread(task, "一号窗口");
        Thread thread2 = new Thread(task, "二号窗口");
        Thread thread3 = new Thread(task, "三号窗口");
        Thread thread4 = new Thread(task, "四号窗口");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
