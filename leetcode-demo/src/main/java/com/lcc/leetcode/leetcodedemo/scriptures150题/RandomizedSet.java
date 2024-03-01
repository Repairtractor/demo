package com.lcc.leetcode.leetcodedemo.scriptures150题;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RandomizedSet {

    /**
     * 存储值：数组下标
     */
    Map<Integer, Integer> map;

    ArrayList<Integer> arr;

    ThreadLocalRandom random = ThreadLocalRandom.current();

    int size = 0;


    /**
     * Initialize your data structure here.
     */
    public RandomizedSet() {
        map = new HashMap<>();
        arr = new ArrayList<>();
    }

    /**
     * Inserts a value to the set. Returns true if the set did not already contain the specified element.
     */
    public boolean insert(int val) {
        if (map.containsKey(val)) {
            return false;
        }
        arr.add(size, val);
        map.put(val, size++);
        return true;
    }

    /**
     * Removes a value from the set. Returns true if the set contained the specified element.
     */
    public boolean remove(int val) {
        if (!map.containsKey(val)) return false;
        Integer index = map.get(val);
        Integer last = arr.get(--size);
        arr.set(index, last);
        map.put(last, index);
        map.remove(val);
        return true;
    }

    /**
     * Get a random element from the set.
     */
    public int getRandom() {
        return arr.get(random.nextInt(size));
    }
}
