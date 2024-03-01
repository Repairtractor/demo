package com.lcc.leetcode.leetcodedemo.scriptures150题;


public class 移除元素27 {
    public int removeElement(int[] nums, int val) {
        int l = 0, r = nums.length;//确定两个指针，[l..r]表示不等于val的元素，依次递增。r指针表示等于val的数据[r...nums.length-]，依次递减扩大
        while (l < r) {
            if (nums[l] != val)
                l++;
            else swap(nums, l, --r);
        }
        return r;
    }

    public static void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }
    public static void main(String[] args) {
        int[] ints = {1, 2, 3, 3, 4};
        int i = new 移除元素27().removeElement(ints, 3);
        System.out.println(i);
        for (int anInt : ints) {
            System.out.println(anInt);
        }

    }
}
