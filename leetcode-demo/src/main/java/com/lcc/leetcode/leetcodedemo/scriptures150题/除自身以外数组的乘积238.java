package com.lcc.leetcode.leetcodedemo.scriptures150题;

public class 除自身以外数组的乘积238 {
    public int[] productExceptSelf(int[] nums) {
        //前缀乘机与后缀乘机
        // 2 3 4 1 5
        // 1 2 6 24 120
        //60 20 5  5  1

        //首先是有两个数组，left和right，left填充前面几个数的乘积，right填充后面到前的乘积，那么当前i的乘积就是，left[i]*right[i]的乘积，秒呀
        int[] l = new int[nums.length], r = new int[nums.length], ans = new int[nums.length];
        l[0] = 1;
        r[r.length - 1] = 1;

        //填充left和right的乘积
        for (int i = 1; i < nums.length; i++) {
            l[i] = l[i - 1] * nums[i - 1];
        }
        for (int i = nums.length - 2; i >= 0; i--) {
            r[i] = r[i + 1] * nums[i + 1];
        }

        for (int i = 0; i < nums.length; i++) {
            ans[i] = l[i] * r[i];
        }
        return ans;
    }

    /**
     * 时间复杂度o(N)，空间复杂度O(1)
     *
     * @param nums
     * @return
     */
    public int[] productExceptSelf1(int[] nums) {
        //前缀乘机与后缀乘机
        // 2 3 4 1 5
        // 1 2 6 24 120
        //60 20 5  5  1

        //首先是有两个数组，left和right，left填充前面几个数的乘积，right填充后面到前的乘积，那么当前i的乘积就是，left[i]*right[i]的乘积，秒呀
        int[] l = new int[nums.length];
        l[0] = 1;
        //填充left和right的乘积
        for (int i = 1; i < nums.length; i++) {
            l[i] = l[i - 1] * nums[i - 1];
        }
        int next = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            l[i] = next * l[i];
            next = next * nums[i];
        }
        return l;
    }


    public static void main(String[] args) {
        int[] ints = new 除自身以外数组的乘积238().productExceptSelf1(new int[]{2, 3, 4, 1, 5});
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}



