package com.lcc.local.localdemo.fail100;


import cn.hutool.core.lang.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class ConcurrentHashMapTest {


    //线程个数
    private static int THREAD_COUNT = 10;
    //总元素数量
    private static int ITEM_COUNT = 1000;

    //帮助方法，用来获得一个指定元素数量模拟数据的ConcurrentHashMap
    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }


    @Test
    public void test() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        //初始900个元素
        log.info("init size:{}", concurrentHashMap.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        //使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            synchronized (concurrentHashMap) {

                //查询还需要补充多少个元素
                int gap = ITEM_COUNT - concurrentHashMap.size();
                log.info("gap size:{}", gap);
                //补充元素
                concurrentHashMap.putAll(getData(gap));
            }
        }));
        //等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //最后元素个数会是1000吗？
        log.info("finish size:{}", concurrentHashMap.size());
    }

    /**
     * 累加计数器
     */
    @SneakyThrows
    @Test
    public void test2() {
//        Map<String, Long> map = new ConcurrentHashMap<>();
        Map<String, Long> map = new HashMap<>();
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);

        forkJoinPool.execute(() -> {
            IntStream.rangeClosed(1, ITEM_COUNT).parallel().forEach(i -> {
                //获得一个随机的Key
                String key = "item" + ThreadLocalRandom.current().nextInt(10);
                //如果不加这个锁，会发生什么？ 会发生打印的值远远小于 item_count
//                synchronized (map) {
//                    Long value = map.get(key);
//                    if (value == null) {
//                        map.put(key, 1L);
//                    } else {
//                        //这里不用原子类会发生什么呢？
//                        map.put(key, value + 1);
//                    }
//                }
                //使用computeIfAbsent方法，可以避免上面的加锁操作,compute在concurrentHashMap中是原子操作,锁住了对应的节点桶， hashMap不是,
                map.compute(key, (k, v) -> v == null ? 1 : v + 1);


            });
        });

        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        map.forEach((k, v) -> log.info("{}:{}", k, v));
        //总和是否大于item_count
        assert map.values().stream().mapToLong(Long::longValue).sum() == ITEM_COUNT;
    }


}
