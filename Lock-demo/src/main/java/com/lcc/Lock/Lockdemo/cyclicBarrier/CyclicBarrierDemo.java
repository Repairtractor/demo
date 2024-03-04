package com.lcc.Lock.Lockdemo.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * cyclicBarrier其实也相当于计数器，只是比CountDownLatch 更好用的是，可以重复使用，并且可以设置回调函数。
 * 其作用都是设置线程屏障，等待其他线程执行完毕，当线程都在等待时，有一个线程被中断，不能到达屏障点，那么所有线程都会抛出异常
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {


    }

    /**
     * 测试cyclicBarrier特性1：线程屏障
     */
    public static void testCyclicBarrier1() {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(3,()->System.out.println("线程执行完毕"));
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("等待线程1");
                cyclicBarrier.await();
                System.out.println("线程1执行完毕");
            } catch (Exception e) {
                System.out.println("线程1执行完毕");
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("等待线程2");
                cyclicBarrier.await();
                System.out.println("线程2执行完毕");
            } catch (Exception e) {
                System.out.println("线程2执行完毕");
            }
        });

        thread1.start();
        thread2.start();
    }
}
