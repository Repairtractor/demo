package com.lcc.leetcode.leetcodedemo.hot100;

public class 删除有序数组中的重复项26 {
    public int removeDuplicates(int[] nums) {
        if (nums.length < 2)
            return nums.length;
        //明确两个指针，slow,fast从头开始扫描，[l...slow-]表示已经有序非重复的数据，前闭后开。[fast...r-]表示不相等的数据，前闭后开
        int slow = 0, fast = 0;
        while (fast < nums.length) {
            if (nums[slow] == nums[fast]) {
                fast++;
            } else {
                nums[++slow] = nums[fast++];
            }
        }
        return slow + 1;
    }

    public static void main(String[] args) {
        int[] ints = {1, 1, 2};
        int i = new 删除有序数组中的重复项26().removeDuplicates(ints);
        System.out.println(i);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}
