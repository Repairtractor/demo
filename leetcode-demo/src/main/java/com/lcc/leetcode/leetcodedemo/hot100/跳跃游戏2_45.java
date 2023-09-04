package com.lcc.leetcode.leetcodedemo.hot100;

public class 跳跃游戏2_45 {
    public int jump(int[] nums) {
        //最小跳跃次数就是保证除了第一个位置之外，下一次的跳跃必须是跳跃范围内的最大值，也就是 max(i+nums[i]),dp[i]=max(dp[i-1],i+nums[i]), dp[i]=min(dp[i-1]
        int max = nums[0], count = 0, num = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (i > num) {
                //说明跳跃超过最大值了
                num = nums[i];
                count++;
            }
            max = Math.max(max, i + nums[i]);
        }
        return count;
    }

    public static void main(String[] args) {
        int jump = new 跳跃游戏2_45().jump(new int[]{2, 3, 1, 1, 4});
        System.out.println(jump);
    }

}
