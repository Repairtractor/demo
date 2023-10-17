package com.lcc.leetcode.leetcodedemo.经典150题;

public class 多数元素169 {
    public int majorityElement(int[] nums) {
        //投票法，首先一个人做最大的那个，然后当有其他出现时抵消改擂主，重新当擂主，如果相等擂主票数加1,知道最后的擂主就是最大重复的
        int count = 1; //擂主数量
        int num = nums[0]; //擂主
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == num) {
                count++;
            } else {
                count--;
                //这里其实有一个问题就是如果有一个数据很多，那么其他的不同的数据会抵消掉改数据，这里就要引出本题的关键点，多数总是大于n/2，
                // 也就是说多数多到大于总数的一半，那么就不会随意抵消了
                if (count == 0) {
                    num = nums[i];
                    count++;
                }
            }
        }
        return num;
    }
}
