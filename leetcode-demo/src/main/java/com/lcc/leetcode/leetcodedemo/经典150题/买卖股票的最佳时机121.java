package com.lcc.leetcode.leetcodedemo.经典150题;

public class 买卖股票的最佳时机121 {
    public int maxProfit(int[] prices) {
        //只需要记录一个历史最小值，然后记录最小值与另一个值的最大差值就行了
        int minPrice = Integer.MAX_VALUE, maxDifference = 0;

        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;
            } else if (price - minPrice > maxDifference) {
                maxDifference = price - minPrice;
            }
        }

        return maxDifference;
    }

    public int maxProfit1(int prices[]) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice) {
                minprice = prices[i];
            } else if (prices[i] - minprice > maxprofit) {
                maxprofit = prices[i] - minprice;
            }
        }
        return maxprofit;
    }


    public static void main(String[] args) {
        int i = new 买卖股票的最佳时机121().maxProfit(new int[]{2, 1, 2, 1, 0, 1, 2});
        System.out.println(i);
    }
}
