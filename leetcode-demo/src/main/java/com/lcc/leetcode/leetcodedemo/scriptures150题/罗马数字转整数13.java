package com.lcc.leetcode.leetcodedemo.scriptures150题;

public class 罗马数字转整数13 {
    public int romanToInt(String s) {
        //记住，如果当前值大于等于下一个值：+  小于：-  ，同时加上最后的值
        char[] charArray = s.toCharArray();
        int sum = 0, preNum = getValue(charArray[0]);

        for (int i = 1; i < charArray.length; i++) {
            int num = getValue(charArray[i]);
            if (preNum < num) {
                sum -= preNum;
            } else
                sum += preNum;
            preNum = num;
        }

        //加上最后的值
        sum+=preNum;

        return sum;
    }

    private int getValue(char ch) {
        switch (ch) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

}
