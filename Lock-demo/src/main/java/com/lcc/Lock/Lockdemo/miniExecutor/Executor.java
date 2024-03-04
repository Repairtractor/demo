package com.lcc.Lock.Lockdemo.miniExecutor;

/**
 * mini executor,这个接口只有执行runnable的行为
 */
public interface Executor {

    /**
     * 线程池执行任务
     * @param runnable 任务
     */
    void execute(Runnable runnable);
}
