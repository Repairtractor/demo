package com.lcc.leetcode.leetcodedemo.review;

public class LeetCode220 {

    public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        return containsNearbyAlmostDuplicateHelper1(nums, indexDiff, valueDiff);
    }

    public static void main(String[] args) {
        LeetCode220 leetCode220 = new LeetCode220();
        boolean b = leetCode220.containsNearbyAlmostDuplicateHelper2(new int[]{1,2,0,1}, 1, 0);

        System.out.println(b);
    }

    /**
     * 暴力破解, O(n^2)的复杂度，会超时
     *
     * @param nums
     * @param indexDiff
     * @param valueDiff
     * @return
     */
    public boolean containsNearbyAlmostDuplicateHelper1(int[] nums, int indexDiff, int valueDiff) {
        if (nums.length == 0)
            return false;

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                //是否满足条件
                if (Math.abs(i - j) <= indexDiff && Math.abs(nums[i] - nums[j]) <= valueDiff)
                    return true;
            }

        }
        return false;

    }


    /**
     * 问题的关键
     * 1. i~j的下标距离不能超过indexDiff
     * 解：基于上面的关键点，可以采用滑动窗口的形式，当i~j的距离等于indexDiff时，就移动i
     * 当i~j小于indexDiff时，移动j进行条件判断
     * 直到i==j
     *
     * @param nums
     * @param indexDiff
     * @param valueDiff
     * @return
     */
    public boolean containsNearbyAlmostDuplicateHelper2(int[] nums, int indexDiff, int valueDiff) {
        if (nums.length <= 1) return false;

        int i = 0, j = 1;
        while (i < j) {
            if (Math.abs(nums[i] - nums[j]) <= valueDiff)
                return true;
            if (Math.abs(i - j) < indexDiff && j < nums.length-1)
                j++;
            else {
                i++;
            }

        }

        return false;

    }


}
