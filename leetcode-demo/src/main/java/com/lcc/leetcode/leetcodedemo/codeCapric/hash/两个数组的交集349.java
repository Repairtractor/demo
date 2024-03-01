package com.lcc.leetcode.leetcodedemo.codeCapric.hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class 两个数组的交集349 {
    public int[] intersection(int[] nums1, int[] nums2) {
        //数组交集，因为数组数字特别大，用数组表示hash会造成空间的浪费，所以用hashMap
        Set<Integer> set=new HashSet<>();
        for (int i : nums1) {
            set.add(i);
        }
        int[]result=new int[nums1.length+nums2.length];
        int curr=0;
        for (int i : nums2) {
            if (set.contains(i)){
                result[curr++]=i;
            }
        }
        return result;

    }
}
