package com.lcc.leetcode.leetcodedemo.scriptures150题;

//给你两个按 非递减顺序 排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n ，分别表示 nums1 和 nums2 中的元素数目。
//
// 请你 合并 nums2 到 nums1 中，使合并后的数组同样按 非递减顺序 排列。
//
// 注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。为了应对这种情况，nums1 的初始长度为 m + n，其中前 m 个元素表示应合并
//的元素，后 n 个元素为 0 ，应忽略。nums2 的长度为 n 。
//
//
//
// 示例 1：
//
//
//输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
//输出：[1,2,2,3,5,6]
//解释：需要合并 [1,2,3] 和 [2,5,6] 。
//合并结果是 [1,2,2,3,5,6] ，其中斜体加粗标注的为 nums1 中的元素。
//
//
// 示例 2：
//
//
//输入：nums1 = [1], m = 1, nums2 = [], n = 0
//输出：[1]
//解释：需要合并 [1] 和 [] 。
//合并结果是 [1] 。
//
//
// 示例 3：
//
//
//输入：nums1 = [0], m = 0, nums2 = [1], n = 1
//输出：[1]
//解释：需要合并的数组是 [] 和 [1] 。
//合并结果是 [1] 。
//注意，因为 m = 0 ，所以 nums1 中没有元素。nums1 中仅存的 0 仅仅是为了确保合并结果可以顺利存放到 nums1 中。
//
//
//
//
// 提示：
//
//
// nums1.length == m + n
// nums2.length == n
// 0 <= m, n <= 200
// 1 <= m + n <= 200
// -10⁹ <= nums1[i], nums2[j] <= 10⁹
//
//
//
//
// 进阶：你可以设计实现一个时间复杂度为 O(m + n) 的算法解决此问题吗？
//
// Related Topics 数组 双指针 排序 👍 2089 👎 0

public class merge88 {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        //可以倒着进行处理，因为已经预留了位置
        int a = nums1.length - 1; //a标识最大值存放的位置，从nums1的结尾开始
        m = m - 1;
        n = n - 1;
        while (n >= 0) {
            if (!(m >= 0)) {
                nums1[a--] = nums2[n--];
                continue;
            }
            if (nums1[m] > nums2[n]) {
                nums1[a--] = nums1[m--];
            } else
                nums1[a--] = nums2[n--];
        }

    }

    public void merge1(int[] nums1, int m, int[] nums2, int n) {
        if (nums1.length == 0)
            return;
        //两个指针，a从0 开始便利nums1 前闭后开 b 遍历nums2 前闭后开 ,使用一个与nums1相同大小的数组存储，避免移动位置
        int a = 0, b = 0, k = 0;
        int[] temps = new int[nums1.length];
        while (a < m || b < n) {
            if (!(b < n)) {
                temps[k++] = nums1[a++];
                continue;
            }

            if (!(a < m) || nums1[a] > nums2[b]) {
                temps[k++] = nums2[b];
                b++;
            } else {
                temps[k++] = nums1[a];
                a++;
            }
        }
        System.arraycopy(temps, 0, nums1, 0, nums1.length);
    }


    public static void main(String[] args) {
        //2 3 5  1 2 3
        int[] ints = {1, 2, 3, 0, 0, 0};
        new merge88().merge(ints, 3, new int[]{2, 5, 6}, 3);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }


}

