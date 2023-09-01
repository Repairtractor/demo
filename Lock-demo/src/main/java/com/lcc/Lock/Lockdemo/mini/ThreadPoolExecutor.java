package com.lcc.Lock.Lockdemo.mini;

import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class ThreadPoolExecutor extends AbstractExecutor {

    /**
     * 线程集合
     */
    private Set<Worker> workers;

    private int corePoolSize;

    /**
     * 任务集合
     */
    private BlockingQueue<Runnable> queue;

    private int runableCapacity;


    public ThreadPoolExecutor(int corePoolSize, int runableCapacity) {
        this.corePoolSize = corePoolSize;
        this.runableCapacity = runableCapacity;
        this.workers = new HashSet<>(corePoolSize);
        this.queue = new ArrayBlockingQueue<>(runableCapacity);
    }

    @Override
    public void execute(Runnable runnable) {

        //线程数少于核心线程数，直接创建
        if (workers.size() < corePoolSize) {
            Worker worker = new Worker(runnable);
            Thread task = worker.getTask();
            task.start();
            workers.add(worker);
            return;
        }
        //判断池子中有没有空闲的线程,有空闲的线程直接执行改任务
        Optional<Worker> any = workers.stream().filter(Worker::isFree).findAny();
        if (any.isPresent()) {
            Worker worker = any.get();
            worker.setRunnable(runnable);
            worker.pursue();
            return;
        }
        //如果没有空闲的线程，需要放入等待队列
        if (queue.offer(runnable)) {
            //这里放入队列成功了 todo 可以做点什么
            LogUtils.info("任务已放入等待队列中");
            return;
        }
        //如果队列也满了，按照jdk的线程池就需要创建新的线程然后执行
        //todo 这里就要想想怎么办了，先放着
        LogUtils.error("队列已满，请稍后提交任务！");
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 100);
        for (int i = 0; i < 10; i++) {
            ThreadUtil.sleep(500);
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                LogUtils.info("当前正在执行任务：{},打印一下当前的线程id:{}", finalI, Thread.currentThread().getName());
                ThreadUtil.sleep(1000);
            });
        }
    }


    /**
     * 线程池中真正执行的线程对象
     */
    @Data
    final class Worker implements Runnable {


        //真正执行的线程
        private final Thread task;

        //任务
        private Runnable runnable;

        //线程是否空闲的标志，线程执行前CAS设置为false，执行完成之后原子设置为true
        private AtomicBoolean isFree = new AtomicBoolean(true);


        public Worker(Runnable runnable) {
            this.runnable = runnable;
            this.task = new Thread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }

        public boolean isFree() {
            return isFree.get();
        }


        public void pursue() {
            LogUtils.info("启动被挂起的线程！" + task.getName());
            task.interrupt();
        }
    }

    void runWorker(Worker worker) {
        for (; ; ) {
            try {
                //如果runnable为空，从队列获取，这里是为了当有空闲线程时，让空闲线程直接执行任务，而不是只能放入队列等待
                if (worker.runnable==null){
                    Runnable take = queue.take();
                    LogUtils.info("从队列获取到数据，开始执行");
                    worker.setRunnable(take);
                }
                //设置任务为非空闲状态
                worker.isFree.compareAndSet(true, false);
                worker.runnable.run();
                //将线程对象的任务设置为空，会从队列获取
                worker.setRunnable(null);
                //执行完任务之后挂起该线程,使用CAS设置线程挂起标志
                worker.isFree.compareAndSet(false, true);
            } catch (InterruptedException e) {
                LogUtils.info("线程被强制唤醒了，开始执行任务");
                //可以强制打断空闲的线程，执行新加入的任务
                runWorker(worker);
            }
        }
    }
}





