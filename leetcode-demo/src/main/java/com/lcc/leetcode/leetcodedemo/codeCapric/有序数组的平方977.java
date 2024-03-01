package com.lcc.leetcode.leetcodedemo.codeCapric;


/**
 * 给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。
 */
public class 有序数组的平方977 {
    public int[] sortedSquares(int[] nums) {
        //非递减顺序，那就是递增的？主要是需要考虑一个负数的平方可能很大
        //如果使用头尾两个指针的话，只需要比较头尾两个指针的绝对值大小，就可以确定最大值的位置了
        //设l 和 r 两个指针，分别重头尾开始，比较大小，绝对值大的平方一定最大，放在新数组的尾部r1

        int l = 0, r = nums.length - 1, r1 = r;
        int[] ans = new int[nums.length];

        while (l <= r) {
            if (Math.abs(nums[l]) > Math.abs(nums[r])) {
                //大的值放入新的数组尾部
                ans[r1] = nums[l] * nums[l];
                l++;
                r1--;
            } else {
                ans[r1]=nums[r]*nums[r];
                r--;
                r1--;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums={-4,-1,0,3,10};
        for (int i : new 有序数组的平方977().sortedSquares(nums)) {
            System.out.print(i+"\t");
        }
    }

}
