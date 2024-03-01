package com.lcc.leetcode.leetcodedemo.codeCapric;

public class 长度最小的子数组209 {

    public int minSubArrayLen(int target, int[] nums) {
        //两个指针slow,fast表示nums[l...r]就是符合结果的值，然后使用一个min表示最小的长度，每次找到的结果值长度与min比较，直到遍历整个数组
        int slow = 0, fast = 0, min = 0, sum = nums[fast];

        //两个快慢指针slow，fast，当nums[slow...fast]符合结果值时，记录长度min，
        // 然后slow++,此时如果nums[slow...fast]已经不满足结果值，就让fast++，
        // 反之记录结果值继续前面的操作，直到遍历完整个数组，也就是r已经不能前进为止

        while (fast < nums.length) {
            if (sum >= target) {
                int lenght = fast + 1 - slow;//长度获取
                if (min == 0)
                    min = lenght;
                else {
                    min = Math.min(min, lenght);
                }
                sum -= nums[slow];
                slow++;
            } else {
                fast++;
                if (fast >= nums.length) return min;
                sum += nums[fast];
            }

        }
        return min;
    }

    public static void main(String[] args) {
        System.out.println(new 长度最小的子数组209().minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));

    }

}
