package com.lcc.leetcode.leetcodedemo.hot100;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        //暴力解很简单，O(N^2)级别
        return new int[]{};
    }


    public int[] twoSum1(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer i1 = map.get(target - nums[i]);
            if (Objects.isNull(i1)) {
                map.put(nums[i], i);
                continue;
            }
            return new int[]{i1,i };
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        int[] ints = new TwoSum().twoSum1(new int[]{2, 3, 7}, 10);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }


}
