package com.lcc.leetcode.leetcodedemo.hot100;

public class 跳跃游戏2_45 {
    public int jump(int[] nums) {
        if (nums.length < 2) {
            return 0;
        }

        //最小跳跃次数就是保证除了第一个位置之外，下一次的跳跃必须是跳跃范围内的最大值，也就是 max(i+nums[i]),dp[i]=max(dp[i-1],i+nums[i]), dp[i]=min(dp[i-1]
        int max = 0, border = 0, count = 0;
        for (int i = 0; i < nums.length-1; i++) {
            max = Math.max(max, nums[i] + i);
            if (i == border) {
                //不需要去选择范围内的位置下标，只需要知道范围内最大能跳跃的位置就好了,而且当超出边界的时候，说明需要跳跃了
                count++;
                border = max; //请记住max表示什么，它表示一段范围内的最大跳跃位置，那么这里赋值就是因为下一次的边界就是最大跳跃的位置
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int jump = new 跳跃游戏2_45().jump(new int[]{2,3,1,1,4});
        System.out.println(jump);
    }

}
