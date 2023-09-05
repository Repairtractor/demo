package com.lcc.Lock.Lockdemo.mini;

import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolExecutor extends AbstractExecutor {

    /**
     * 线程集合
     */
    private Set<Worker> workers;

    /**
     * 核心线程数量
     */
    private final int corePoolSize;

    /**
     * 最大线程数量
     */
    private final int maximumPoolSize;


    /**
     * 任务阻塞队列
     */
    private BlockingQueue<Runnable> workQueue;


    /**
     * 拒绝策略
     */
    private final RejectedHandler rejectedHandler;

    /**
     * 空闲线程回收时间
     */
    private final long expire;

    private TimeUnit unit;

    /**
     * 核心线程是否空闲时回收
     */
    private boolean allowCoreThreadTimeOut;

    /**
     * 线程池停止
     */
    private volatile AtomicBoolean showdown;


    /**
     * 可重入锁
     */
    private final ReentrantLock mainLock = new ReentrantLock();


    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long expire, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedHandler rejectedHandler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workers = new HashSet<>(corePoolSize);
        this.workQueue = workQueue;
        this.rejectedHandler = rejectedHandler;
        this.expire = expire;
        this.unit = unit;
    }

    @Override
    public void execute(Runnable runnable) {

        //线程池是否在运行
        if (!isRunning()) {
            LogUtils.info("线程池关闭，拒绝执行改任务");
            rejectedHandler.rejectedExecution(runnable, this);
        }

        if (workers.size() < corePoolSize) {
            //线程数少于核心线程数，直接创建
            addWorker(runnable, true);
            return;
        }

        //如果没有空闲的线程，需要放入等待队列
        if (workQueue.offer(runnable)) {
            //这里放入队列成功了 todo 可以做点什么
            LogUtils.info("任务已放入等待队列中");
            return;
        }
        //如果队列也满了，按照jdk的线程池就需要创建新的线程然后执行
        if (addWorker(null, false)) {
            LogUtils.info("队列满了！但是线程数量少于最大线程数，创建新线程执行任务");
            execute(runnable);
            return;
        }
        // 线程数也大于最大线程，队列也满了，需要执行拒绝策略
        rejectedHandler.rejectedExecution(runnable, this);
    }

    /**
     * 中断所有正在执行的任务，返回所有还没有开始的任务
     *
     * @return
     */
    public List<Runnable> shutdownNow() {
        mainLock.lock();
        ArrayList<Runnable> runnables = new ArrayList<>();
        try {

            boolean b = showdown.compareAndSet(false, true);
            if (b) {
                //设置标识量为true，成功之后
                //中断所有正在执行的线程
                for (Worker it : workers) {
                    it.task.interrupt();
                }
                workQueue.drainTo(runnables);
            }

        } finally {
            mainLock.unlock();
        }
        return runnables;
    }


    /**
     * 执行任务，
     *
     * @param firstTask 如果runnable不为空，将会将改任务作为第一个任务执行，为空会创建线程对象执行队列中的任务
     * @param core
     * @return
     */
    private boolean addWorker(Runnable firstTask, boolean core) {

        //这里需要再次判断，并且判断完成枷锁
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (core ? workers.size() > corePoolSize : workers.size() > maximumPoolSize) {
                return false;
            }

            //如果当前线程数量超过了核心线程数的一般，默认先查询空闲线程
            Worker worker = null;

            if ((workers.size() < corePoolSize / 2)) {
                for (Worker item : workers) {
                    if (item.isFree()) {
                        worker = item;
                        worker.setRunnable(firstTask);
                        worker.pursue();
                    }

                }
            }

            if (worker == null) {
                worker = new Worker(firstTask);
                worker.getTask().start();
                workers.add(worker);
            }
        } finally {
            mainLock.unlock();
        }

        return true;
    }


    /**
     * 线程池中真正执行的线程对象
     */
    void runWorker(Worker worker) {
        for (; ; ) {
            try {
                //如果runnable为空，从队列获取，这里是为了当有空闲线程时，让空闲线程直接执行任务，而不是只能放入队列等待
                if (worker.runnable == null) {
                    LogUtils.info("从队列获取到数据，开始执行");
                    Runnable take = timeout() ? workQueue.poll(expire, unit) : workQueue.take();
                    worker.setRunnable(take);
                    if (take == null) {
                        throw new InterruptedException();
                    }
                }
                //设置任务为非空闲状态
                worker.isFree.compareAndSet(true, false);
                worker.runnable.run();
                //将线程对象的任务设置为空，会从队列获取
                worker.setRunnable(null);
                //执行完任务之后挂起该线程,使用CAS设置线程挂起标志
                worker.isFree.compareAndSet(false, true);
            } catch (InterruptedException e) {
                if (worker.runnable == null) {
                    LogUtils.info("线程空闲时间较长，回收线程");
                    workers.remove(worker);
                } else {
                    LogUtils.info("线程被强制唤醒了，开始执行任务");
                    //可以强制打断空闲的线程，执行新加入的任务
                    runWorker(worker);
                }
            }
        }
    }

    private boolean timeout() {
        return allowCoreThreadTimeOut || workers.size() > corePoolSize;
    }

    /**
     * 设置线程池结束
     */
    private void shutdown() {
        this.showdown.compareAndSet(false, true);
    }


    private boolean isRunning() {
        return !showdown.get();
    }


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


    public static class ThrowExceptionHandler implements RejectedHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            throw new RuntimeException("任务实在太多了！触发了拒绝策略，当前线程池拒绝策略为：抛出异常！");
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3), new ThrowExceptionHandler());
        for (int i = 0; i < 10; i++) {
            ThreadUtil.sleep(100);
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                LogUtils.info("当前正在执行任务：{},打印一下当前的线程id:{}", finalI, Thread.currentThread().getName());
                ThreadUtil.sleep(1000);
            });
        }
    }
}





