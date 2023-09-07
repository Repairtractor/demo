package com.lcc.leetcode.leetcodedemo.hot100;

import java.util.Arrays;

public class 加油站134 {
    public int canCompleteCircuit(int[] gas, int[] cost) {
//输入: gas = [1,2,3,4,5], cost = [3,4,5,1,2] 输出: 3
        //有一个数temp表示含油量，temp+=gas[i] 如果temp大于cost[i],说明油量充足，继续走，当走到起始下标时停下，返回ans
        //暴力解 AC不掉
        boolean flag = true;
        for (int start = 0; start < gas.length; start++) {
            int temp = 0, i = start;

            do {
                temp += gas[i];
                if (temp < cost[i]) {
                    flag = false;
                    break;
                }
                temp -= cost[i];
                i++;
                if (i == gas.length) {
                    i = i % gas.length;
                }

            } while (i != start);
            if (flag)
                return start;
            else
                flag = true;
        }
        return -1;

    }


    /**
     * @param gas
     * @param cost
     * @return
     */
    public int canCompleteCircuit1(int[] gas, int[] cost) {
        //使用滑动窗口，gas[l...r]表示油量总和，cost[l...r]表示消耗油量，当油量少于消耗油量时，l++,大于时 r++，直到r=l
        int lrsum = gas[0], lrdeplete = cost[0], l = 0, r = 0, total = 0;
        do {
            if (lrsum >= lrdeplete) {
                r++;
                if (r == gas.length) {
                    r = (r) % gas.length;
                    total++;
                }
                lrsum += gas[r];
                lrdeplete += cost[r];
            } else {
                lrsum -= gas[l];
                lrdeplete -= cost[l];
                l++;
            }
            if (l == r) {
                if (total == 1) break;
            }
        } while (l < gas.length);

        if (!(l < gas.length)) {
            return -1;
        }
        return l;
    }

    public int canCompleteCircuit2(int[] gas, int[] cost) {

        if (Arrays.stream(gas).sum() < Arrays.stream(cost).sum()) {
            return -1;
        }

        int left = 0;
        for (int right = 0, remainder = 0; right < gas.length; right++) {
            if ((remainder += gas[right] - cost[right]) < 0) {
                left = right + 1;
                remainder = 0;
            }
        }
        return left;

    }

    public static void main(String[] args) {
        int i = new 加油站134().canCompleteCircuit1(new int[]{2, 3, 4}, new int[]{3, 4, 3});
        System.out.println(i);

        //2,3,4
        //3,4,3
        //-1 -1 1

        //1, 2, 3, 4, 5, 4, 3
        //3, 4, 5, 1, 2, 3, 4
        //-2 -2 -2 3, 3 3  1,-1

        //8, 2 ,6
        //2, 9 ,3


    }
}
