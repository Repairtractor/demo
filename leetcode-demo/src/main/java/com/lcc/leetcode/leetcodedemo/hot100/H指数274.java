package com.lcc.leetcode.leetcodedemo.hot100;

import java.util.Arrays;

public class H指数274 {
    public int hIndex(int[] citations) {
        //全部加起来除以长度？ h指数是指至少有H篇文章被引用的h次，也就是说长度要与平均值一样
        //如果我有一个桶进行遍历，那么每次当他大于这个桶的下标的时候就放进去，最后找出下标与数值一样的就是H
        int[] temp = new int[citations.length + 1]; //桶，值为存放进入数据的次数

        for (int i = 0; i < citations.length; i++) {
            int min = Math.min(temp.length, citations[i] + 1);
            for (int i1 = 1; i1 < min; i1++) {
                temp[i1] += 1;
            }
        }

        for (int i = temp.length - 1; i > 0; i--) {
            if (temp[i] == i || i < temp[i]) return i;
        }
        return 0;
    }

    class Solution {
        public int hIndex(int[] citations) {
            Arrays.sort(citations);
            int h = 0, i = citations.length - 1;
            while (i >= 0 && citations[i] > h) {
                //因为是进行了排序，
                //那么后面的肯定会加入到h的平均数+1中，当数量增加1时，那么h也应该前进。这个解法的主要是排序以及h与下标和平均值对应
                h++;
                i--;
            }
            return h;
        }
    }



    public static void main(String[] args) {
        int i = new H指数274().hIndex(new int[]{1, 2, 3});
        System.out.println(i);
    }
}
