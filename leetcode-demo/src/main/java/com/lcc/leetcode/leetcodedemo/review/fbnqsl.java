package com.lcc.leetcode.leetcodedemo.review;

public class fbnqsl {
    public static void main(String[] args) {
        Solution2 solution2 = new Solution2();
        System.out.println(solution2.maxProfit(new int[]{7,1,5,3,6,4}));
    }


    class Solution {
        public int climbStairs(int n) {
            int[] dp = new int[n + 1];
            if (n <= 1) {
                return n;
            }
            dp[1] = 1;
            dp[2] = 2;
            for (int i = 3; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }

    static class Solution1 {
        public int fib(int n) {
            if (n <= 1) {
                return n;
            }
            int[] dp = new int[n+1];
            dp[1] = 1;
            dp[2] = 1;
            for (int i = 3; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }

  static   class Solution2 {
        ////只需要记录一个历史最小值，然后记录最小值与另一个值的最大差值就行了
        /// [7,1,5,3,6,4]
        public int maxProfit(int[] prices) {
            int min = prices[0],df=0,maxIndex=0,minIndex=0;
            for (int price : prices) {
                if (min > price) {
                    min = price;
                    minIndex = min;
                }
                if (df<price-min){
                    df = price-min;
                    maxIndex=price;
                }
            }
            System.out.println("最小：" + min+"最大："+maxIndex);
            return df;
        }
    }


}
