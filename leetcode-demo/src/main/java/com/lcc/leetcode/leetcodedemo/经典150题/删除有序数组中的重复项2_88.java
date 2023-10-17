package com.lcc.leetcode.leetcodedemo.经典150题;

public class 删除有序数组中的重复项2_88 {
    public int removeDuplicates(int[] nums) {
        if (nums.length < 3) {
            return nums.length;
        }
        //快慢指针，明确slow和fast的语义,[l...slow-]表示所有已经排好序的数据。[fast...r-]标识未扫描过的数据
        int slow = 0, fast = 0;
        if (nums[slow] == nums[slow + 1]) {
            slow += 1;
            fast += 1;
        }

        while (fast < nums.length) {
            while (fast < nums.length && nums[slow] == nums[fast]) {
                fast++;
            }
            if (fast < nums.length) {
                nums[++slow] = nums[fast++];
            }
            if (fast < nums.length && nums[slow] == nums[fast]) {
                nums[++slow] = nums[fast++];
            }
        }
        return slow + 1;
    }


    public int removeDuplicates1(int[] nums) {
        if(nums.length <= 2) return nums.length;
        int slow = 2;
        for (int fast = 2; fast < nums.length; fast++) {
            if (nums[fast] != nums[slow-2]) {
                nums[slow++] = nums[fast];
            }
        }
        return slow;
    }

    public static void main(String[] args) {
        int[] ints = {1, 1, 1, 2, 2, 3}; //0 0 1 1 2 1,3,3
        int i = new 删除有序数组中的重复项2_88().removeDuplicates1(ints);
        System.out.println(i);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}
