package com.lcc.leetcode.leetcodedemo.hot100;

public class 轮转数组189 {
    public void rotate(int[] nums, int k) {

        //乱转数组，不就是一个环？换的移动公式是：下标+k%length
        int[] temps = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            temps[(i + k) % nums.length] = nums[i];
        }
        System.arraycopy(temps, 0, nums, 0, temps.length);
    }

    /**
     * 双指针解法
     *
     * @param nums
     * @param k
     */
    public void rotate1(int[] nums, int k) {
        //双指针解法就是一前以后两个指针，中间间隔为k，调换nums.length次之后数据就是调换之后的数据
        int slow = 0;
        while (slow < k) {
            int fast = k + slow, fastNum = nums[slow];
            for (int i = 0; i < nums.length; i++) {
                int temp = nums[fast];
                nums[fast] = fastNum;
                fastNum = temp;
                fast = (fast + k) % nums.length;
            }
            slow++;
        }
    }


    /**
     * 数组反转
     *
     * @param nums
     * @param k
     */
    public void rotate2(int[] nums, int k) {
        //首先将需要移动到下一环的数据摘出来，直接放在开头，然后按照顺序进行相隔位置的交换
        //使k大于nums.length也可以进行反转
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }


    public static void main(String[] args) {
        int[] ints = {-1, -100, 3, 99, 4};
        new 轮转数组189().rotate2(ints, 2);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
}
