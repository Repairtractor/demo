package com.lcc.leetcode.leetcodedemo.经典150题;

/**
 * 核心思想其实是动态规划，dp[i]记录nums[i]之前所能到达的最远距离，dp[i] = max(dp[i-1], i + nums[i])，空间优化可以将dp[i]变为dp，dp就是题解中的k
 */
public class 跳跃游戏55 {
    public boolean canJump(int[] nums) {
        int k = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > k) return false;
            k = Math.max(k, i + nums[i]);
        }
        return true;
    }



    public static void main(String[] args) {
        boolean b = new 跳跃游戏55().canJump(new int[]{1, 1, 2, 2, 0, 1, 1});
        System.out.println(b);
    }
}
