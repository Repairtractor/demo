package com.lcc.leetcode.leetcodedemo.scriptures150题;

import java.util.Arrays;

public class 分发糖果135 {

    public int candy(int[] ratings) {
        //首先所有人都有一个糖果，然后从左到右 比大小发糖，然后从右到做比大小发糖，最后取两个数组的最大值就好
        int[] left = new int[ratings.length], right = new int[ratings.length];
        Arrays.fill(left, 1);
        Arrays.fill(right, 1);
        //从左到右，能够给所有右边大的孩子分取更多的糖果
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1])
                left[i] = left[i - 1] + 1;
        }

        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                right[i] = right[i + 1] + 1;
            }
        }
        int sum = 0;
        for (int i = 0; i < left.length; i++) {
            sum += Math.max(left[i], right[i]);
        }

        return sum;

    }

    public static void main(String[] args) {
        System.out.println(new 分发糖果135().candy(new int[]{1, 0, 2}));

        /**
         * 2,1,2
         *
         * 1,1,2
         * 2  1  1
         *
         */
    }
}
