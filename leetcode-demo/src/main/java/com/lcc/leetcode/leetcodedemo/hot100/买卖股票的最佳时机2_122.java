package com.lcc.leetcode.leetcodedemo.hot100;

public class 买卖股票的最佳时机2_122 {
    public int maxProfit(int[] prices) {
        //推算状态转移方程
        //最大收益=l~mod的最大收益+mod~r的最大收益 [7,1,5,3,6,4]

        int ans=0;
        for (int i=1;i<prices.length;i++){
            if (prices[i]>prices[i-1])
                ans+=prices[i]-prices[i-1];
        }

        return ans;
    }

    public static void main(String[] args) {
        int i = new 买卖股票的最佳时机2_122().maxProfit(new int[]{7, 1, 5, 3, 6, 4});
        System.out.println(i);
    }


    public class Solution {

        private int res;

        public int maxProfit(int[] prices) {
            int len = prices.length;
            if (len < 2) {
                return 0;
            }
            this.res = 0;
            dfs(prices, 0, len, 0, res);
            return this.res;
        }

        /**
         * @param prices 股价数组
         * @param index  当前是第几天，从 0 开始
         * @param status 0 表示不持有股票，1表示持有股票，
         * @param profit 当前收益
         */
        private void dfs(int[] prices, int index, int len, int status, int profit) {

            if (index == len) {
                this.res = Math.max(this.res, profit);
                return;
            }

            dfs(prices, index + 1, len, status, profit);

            if (status == 0) {
                // 可以尝试转向 1
                dfs(prices, index + 1, len, 1, profit - prices[index]);

            } else {
                // 此时 status == 1，可以尝试转向 0
                dfs(prices, index + 1, len, 0, profit + prices[index]);
            }
        }
    }




}
