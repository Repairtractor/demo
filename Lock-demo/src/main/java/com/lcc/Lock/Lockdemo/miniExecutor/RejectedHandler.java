package com.lcc.Lock.Lockdemo.miniExecutor;

/**
 * 拒绝策略接口
 */
public interface RejectedHandler {

    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);

}
