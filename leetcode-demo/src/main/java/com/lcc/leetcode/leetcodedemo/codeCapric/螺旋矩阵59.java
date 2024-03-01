package com.lcc.leetcode.leetcodedemo.codeCapric;

public class 螺旋矩阵59 {
    public int[][] generateMatrix(int n) {
        //循环不变量？随着循环递进，变量保持的语义不变
        //n是多少，就代表有多少行，多少列
        int[][] nums = new int[n][n];
        //如果分四条边, [l][l...r) 为上边  [l...r)[r]为右边 [r][r...l)为底边  [r...l)[l] 为左边，四条边形成之后继续l+1,r-1的前进
        int l = 0, r = n - 1;
        for (int i = 0; i < n * n; ) {
            //上边
            for (int j = l; j < r; j++) {
                nums[l][j] = ++i;
            }
            //右边
            for (int j = l; j < r; j++) {
                nums[j][r] = ++i;
            }
            //下边
            for (int j = r; j > l; j--) {
                nums[r][j] = ++i;
            }
            //左边
            for (int j = r; j > l; j--) {
                nums[j][l] = ++i;
            }
            if (l == r) {
                nums[l][r] = ++i;
            }

            l++;
            r--;
        }

        return nums;
    }

    public static void main(String[] args) {
        int[][] ints = new 螺旋矩阵59().generateMatrix(4);
        for (int i = 0; i < ints.length; i++) {
            int[] nums = ints[i];
            for (int num : nums) {
                System.out.print(num);
            }
            System.out.println();
        }
    }

}
