package com.lcc.leetcode.leetcodedemo.scriptures150题;

public class 接雨水42 {
    public int trap(int[] height) {

        //设层级level初始为1，依次遍历直到数组中没有大于level的
        //去掉头尾小于level的数据
        int l = 0, r = height.length - 1, level = 1, ans = 0;

        while (true) {
            boolean flag = false;
            while (l < r && height[l] < level) {
                l++;
            }
            while (l < r && height[r] < level) {
                r--;
            }
            if (l >= r) return ans;
            for (int i = l; i < r; i++) {
                if (height[i] < level) {
                    flag = true;
                    ans++;
                }
            }
            level++;
            if (!flag) return ans;
        }
    }

    public static void main(String[] args) {
        int trap = new 接雨水42().trap(new int[]{4,2,3});
        System.out.println(trap);
    }
}
