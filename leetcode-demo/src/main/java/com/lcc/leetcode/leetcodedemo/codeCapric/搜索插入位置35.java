package com.lcc.leetcode.leetcodedemo.codeCapric;

public class 搜索插入位置35 {
    public int searchInsert(int[] nums, int target) {
        //明确边界，使用前闭后闭的边界[l,r] 那么mid=（r-l)/2+l，分别找出[l...mid] [mid+1...r]符合的数据
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int mid = (r - l) / 2 + l;
            if (target < nums[mid]) {
                r = mid - 1;
            } else if (target > nums[mid]) {
                l = mid + 1;
            } else if (target == nums[mid])
                return mid;
        }
        return l;
    }

    public static void main(String[] args) {
        搜索插入位置35 cc = new 搜索插入位置35();
        System.out.println(cc.searchInsert(new int[]{1,3,4},6));
    }
}
